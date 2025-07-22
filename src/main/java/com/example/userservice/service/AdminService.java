package com.example.userservice.service;

import com.example.userservice.dto.CardStatisticsDTO;
import com.example.userservice.dto.GenerateCardsRequest;
import com.example.userservice.dto.RechargeAgentRequest;
import com.example.userservice.entity.*;
import com.example.userservice.exception.*;
import com.example.userservice.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceTransactionRepository transactionRepository;
    private final RechargeCardRepository rechargeCardRepository;

    private static final int BATCH_SIZE = 100;

    @Transactional
    public String rechargeAgentBalance(String adminUsername, RechargeAgentRequest request) {
        validateRechargeAgentRequest(request);

        BalanceTransaction transaction = new BalanceTransaction();
        transaction.setTransactionType("admin-recharge");
        transaction.setAmount(request.getAmount());
        transaction.setStartTime(LocalDateTime.now());
        transaction.setStatus("PENDING");

        try {
            LocalDateTime processStart = LocalDateTime.now();

            User admin = userRepository.findByUsername(adminUsername)
                    .orElseThrow(() -> new UserNotFoundException("Admin with username '" + adminUsername + "' not found"));

            User agent = userRepository.findByUsername(request.getAgentUsername())
                    .orElseThrow(() -> new UserNotFoundException("Agent with username '" + request.getAgentUsername() + "' not found"));

            Balance adminBalance = balanceRepository.findByUser(admin)
                    .orElseThrow(() -> new ResourceNotFoundException("ADMIN_BALANCE_NOT_FOUND", "Balance for admin not found"));

            Balance agentBalance = balanceRepository.findByUser(agent)
                    .orElseThrow(() -> new ResourceNotFoundException("AGENT_BALANCE_NOT_FOUND", "Balance for agent not found"));

            if (adminBalance.getCurrentBalance().compareTo(request.getAmount()) < 0) {
                throw new InsufficientBalanceException("INSUFFICIENT_ADMIN_BALANCE", "Admin has insufficient balance to perform recharge");
            }

            adminBalance.setCurrentBalance(adminBalance.getCurrentBalance().subtract(request.getAmount()));
            agentBalance.setCurrentBalance(agentBalance.getCurrentBalance().add(request.getAmount()));
            balanceRepository.save(adminBalance);
            balanceRepository.save(agentBalance);

            transaction.setSource(admin);
            transaction.setDestination(agent);
            transaction.setStatus("SUCCESS");
            transaction.setEndTime(LocalDateTime.now());
            transaction.setExecutionTime(Duration.between(processStart, transaction.getEndTime()).toMillis());
            transactionRepository.save(transaction);

            return "Recharge successful. Agent balance increased by " + request.getAmount();
        } catch (Exception ex) {
            transaction.setStatus("FAILED");
            transaction.setEndTime(LocalDateTime.now());
            if (transaction.getStartTime() != null) {
                transaction.setExecutionTime(Duration.between(transaction.getStartTime(), transaction.getEndTime()).toMillis());
            }
            transactionRepository.save(transaction);
            throw ex;
        }
    }

    private void validateRechargeAgentRequest(RechargeAgentRequest request) {
        if (request.getAgentUsername() == null || request.getAgentUsername().isBlank()) {
            throw new ValidationException("INVALID_AGENT_USERNAME", "Agent username cannot be empty");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("INVALID_AMOUNT", "Recharge amount must be greater than zero");
        }
    }

    @Transactional
    @CacheEvict(value = "cardCounts", key = "#request.cardValue.toString()")
    public List<RechargeCard> generateRechargeCards(GenerateCardsRequest request) {
        validateGenerateCardsRequest(request);

        List<RechargeCard> cards = new ArrayList<>();
        int remainingQuantity = request.getQuantity();

        while (remainingQuantity > 0) {
            int batchSize = Math.min(remainingQuantity, BATCH_SIZE);
            List<RechargeCard> batch = new ArrayList<>();
            for (int i = 0; i < batchSize; i++) {
                RechargeCard card = new RechargeCard();
                card.setCode(generateUniqueCardCode());
                card.setValue(request.getCardValue());
                card.setUsed(false);
                batch.add(card);
            }
            rechargeCardRepository.saveAll(batch);
            cards.addAll(batch);
            remainingQuantity -= batchSize;
        }

        return cards;
    }

    private void validateGenerateCardsRequest(GenerateCardsRequest request) {
        if (request.getAdminUsername() == null || request.getAdminUsername().isBlank()) {
            throw new ValidationException("INVALID_ADMIN_USERNAME", "Admin username cannot be empty");
        }
        if (request.getCardValue() == null || request.getCardValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("INVALID_CARD_VALUE", "Card value must be greater than zero");
        }
        if (request.getQuantity() <= 0) {
            throw new ValidationException("INVALID_QUANTITY", "Quantity must be greater than zero");
        }
    }

    private String generateUniqueCardCode() {
        String code = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        int maxAttempts = 10;
        int attempts = 0;

        while (rechargeCardRepository.existsByCode(code) && attempts++ < maxAttempts) {
            code = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        }

        if (attempts >= maxAttempts) {
            throw new ValidationException("CARD_CODE_GENERATION_FAILED", "Failed to generate unique card code after multiple attempts");
        }

        return code;
    }

    public List<CardStatisticsDTO> getCardStatistics() {
        return rechargeCardRepository.getCardStatistics();
    }

    public CardStatisticsDTO getCardStatisticsByValue(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("INVALID_CARD_VALUE", "Card value must be greater than zero");
        }
        return rechargeCardRepository.getCardStatisticsByValue(value);
    }

    public CardStatisticsDTO getOverallCardStatistics() {
        return rechargeCardRepository.getOverallCardStatistics();
    }
}