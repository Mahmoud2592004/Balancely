package com.example.userservice.service;

import com.example.userservice.entity.*;
import com.example.userservice.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RechargeCardService {

    private final RechargeCardRepository rechargeCardRepository;
    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceTransactionRepository transactionRepository;

    @Transactional
    public List<RechargeCard> buyRechargeCards(String username, String password, BigDecimal value, int quantity) {
        LocalDateTime transactionStart = LocalDateTime.now();

        User posUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!posUser.getPasswordHash().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        Balance posBalance = balanceRepository.findByUser(posUser)
                .orElseThrow(() -> new RuntimeException("Balance not found"));

        BigDecimal totalCost = value.multiply(BigDecimal.valueOf(quantity));
        if (posBalance.getCurrentBalance().compareTo(totalCost) < 0) {
            throw new RuntimeException("Insufficient balance to buy cards");
        }

        BalanceTransaction transaction = new BalanceTransaction();
        transaction.setAmount(totalCost);
        transaction.setTransactionType("card");
        transaction.setSource(posUser);
        transaction.setDestination(posUser);
        transaction.setStatus("PENDING");
        transaction.setStartTime(transactionStart);
        transactionRepository.save(transaction);

        try {
            // Deduct balance
            posBalance.setCurrentBalance(posBalance.getCurrentBalance().subtract(totalCost));
            balanceRepository.save(posBalance);

            // Assign recharge cards
            List<RechargeCard> availableCards = rechargeCardRepository.findByIsUsedFalseAndValue(value);
            if (availableCards.size() < quantity) {
                throw new RuntimeException("Not enough cards of value " + value + " available");
            }

            List<RechargeCard> cardsToAssign = availableCards.subList(0, quantity);
            for (RechargeCard card : cardsToAssign) {
                card.setUsed(true);
                card.setUsedBy(posUser);
                card.setUsedAt(LocalDateTime.now());
            }

            rechargeCardRepository.saveAll(cardsToAssign);

            // Complete transaction
            transaction.setStatus("SUCCESS");
            transaction.setEndTime(LocalDateTime.now());
            transaction.setExecutionTime(Duration.between(transactionStart, transaction.getEndTime()).toMillis());
            transactionRepository.save(transaction);

            return cardsToAssign;
        } catch (Exception e) {
            // Handle failed transaction
            transaction.setStatus("FAILED");
            transaction.setEndTime(LocalDateTime.now());
            transaction.setExecutionTime(Duration.between(transactionStart, transaction.getEndTime()).toMillis());
            transactionRepository.save(transaction);

            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        }
    }

    public List<RechargeCard> getMyCards(Long userId) {
        return rechargeCardRepository.findByUsedById(userId);
    }
}