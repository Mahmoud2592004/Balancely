package com.example.userservice.service;

import com.example.userservice.dto.BuyCardRequest;
import com.example.userservice.dto.CardStatisticsDTO;
import com.example.userservice.entity.*;
import com.example.userservice.exception.*;
import com.example.userservice.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RechargeCardService {
    private final RechargeCardRepository rechargeCardRepository;
    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceTransactionRepository transactionRepository;
    private final TransactionService transactionService;

    private static final int BATCH_SIZE = 100;

    @Transactional
    public List<RechargeCard> buyRechargeCards(String posUsername, BuyCardRequest request) {
        validateBuyCardRequest(request);
        User posUser = userRepository.findByUsername(posUsername)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + posUsername + "' not found"));
        Balance posBalance = balanceRepository.findByUser(posUser)
                .orElseThrow(() -> new ResourceNotFoundException("BALANCE_NOT_FOUND", "Balance for user not found"));

        BigDecimal totalCost = request.getCardValue().multiply(BigDecimal.valueOf(request.getQuantity()));
        if (posBalance.getCurrentBalance().compareTo(totalCost) < 0) {
            throw new InsufficientBalanceException("INSUFFICIENT_BALANCE", "Insufficient balance to buy cards");
        }

        String shard = CardShardSelector.getShard(request.getCardValue());
        if (getAvailableCardCount(request.getCardValue()) < request.getQuantity()) {
            throw new ResourceNotFoundException("INSUFFICIENT_CARDS", "Not enough cards available for value " + request.getCardValue());
        }

        BalanceTransaction transaction = startTransaction(posUser, totalCost);
        try {
            updateBalance(posBalance, totalCost);
            List<RechargeCard> cards = assignCardsInBatches(posUser, request, shard);
            completeTransaction(transaction, "SUCCESS");
            evictCardCountCache(request.getCardValue());
            return cards;
        } catch (Exception e) {
            completeTransaction(transaction, "FAILED");
            throw e;
        }
    }

    @Cacheable(value = "cardCounts", key = "#value.toString()")
    public long getAvailableCardCount(BigDecimal value) {
        return rechargeCardRepository.countByIsUsedFalseAndValue(value);
    }

    @CacheEvict(value = "cardCounts", key = "#value.toString()")
    public void evictCardCountCache(BigDecimal value) {
        // Async cache eviction handled by Spring Cache
    }

    private BalanceTransaction startTransaction(User posUser, BigDecimal totalCost) {
        BalanceTransaction transaction = new BalanceTransaction();
        transaction.setAmount(totalCost);
        transaction.setTransactionType("card");
        transaction.setSource(posUser);
        transaction.setDestination(posUser);
        transaction.setStatus("PENDING");
        transaction.setStartTime(LocalDateTime.now());
        transactionService.logTransaction(transaction);
        return transaction;
    }

    private void updateBalance(Balance posBalance, BigDecimal totalCost) {
        posBalance.setCurrentBalance(posBalance.getCurrentBalance().subtract(totalCost));
        balanceRepository.save(posBalance);
    }

    private List<RechargeCard> assignCardsInBatches(User posUser, BuyCardRequest request, String shard) {
        List<RechargeCard> assignedCards = new ArrayList<>();
        int remainingQuantity = request.getQuantity();
        LocalDateTime now = LocalDateTime.now();

        while (remainingQuantity > 0) {
            int batchSize = Math.min(remainingQuantity, BATCH_SIZE);
            Pageable pageable = PageRequest.of(0, batchSize);
            List<RechargeCard> batchCards = rechargeCardRepository.findTopByIsUsedFalseAndValue(
                    request.getCardValue(), pageable);
            if (batchCards.size() < batchSize) {
                throw new ResourceNotFoundException("INSUFFICIENT_CARDS", "Not enough cards available for value " + request.getCardValue());
            }

            List<Long> cardIds = batchCards.stream().map(RechargeCard::getId).collect(Collectors.toList());
            List<Long> versions = batchCards.stream().map(RechargeCard::getVersion).collect(Collectors.toList());
            int updated = rechargeCardRepository.updateCardsToUsed(cardIds, posUser, now, versions.get(0));
            if (updated != batchSize) {
                throw new OptimisticLockException("Concurrent modification detected for cards (OPTIMISTIC_LOCK_FAILED)");
            }

            assignedCards.addAll(batchCards);
            remainingQuantity -= batchSize;
        }

        return assignedCards;
    }

    private void completeTransaction(BalanceTransaction transaction, String status) {
        transaction.setStatus(status);
        transaction.setEndTime(LocalDateTime.now());
        transaction.setExecutionTime(Duration.between(transaction.getStartTime(), transaction.getEndTime()).toMillis());
        transactionService.logTransaction(transaction);
    }

    private void validateBuyCardRequest(BuyCardRequest request) {
        if (request.getCardValue() == null || request.getCardValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("INVALID_CARD_VALUE", "Card value must be greater than zero");
        }
        if (request.getQuantity() <= 0) {
            throw new ValidationException("INVALID_QUANTITY", "Quantity must be greater than zero");
        }
    }

    public List<RechargeCard> getMyCards(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));
        return rechargeCardRepository.findByUsedById(user.getId());
    }

    public List<CardStatisticsDTO> getMyCardStatistics(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));
        return rechargeCardRepository.findCardStatisticsByUser(user.getId());
    }

    public CardStatisticsDTO getMyCardStatisticsByValue(String username, BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("INVALID_CARD_VALUE", "Card value must be greater than zero");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));
        return rechargeCardRepository.findCardStatisticsByUserAndValue(user.getId(), value);
    }

    public CardStatisticsDTO getMyOverallCardStatistics(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));
        return rechargeCardRepository.findOverallCardStatisticsByUser(user.getId());
    }
}