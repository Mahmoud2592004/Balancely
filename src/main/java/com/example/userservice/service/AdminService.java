package com.example.userservice.service;

import com.example.userservice.dto.CardStatisticsDTO;
import com.example.userservice.dto.GenerateCardsRequest;
import com.example.userservice.dto.RechargeAgentRequest;
import com.example.userservice.entity.*;
import com.example.userservice.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceTransactionRepository transactionRepository;
    private final RechargeCardRepository rechargeCardRepository;

    @Transactional
    public String rechargeAgentBalance(String adminUsername, RechargeAgentRequest request) {
        BalanceTransaction transaction = new BalanceTransaction();
        transaction.setTransactionType("admin-recharge");
        transaction.setAmount(request.getAmount());
        transaction.setStartTime(LocalDateTime.now());
        transaction.setStatus("PENDING");

        try {
            LocalDateTime processStart = LocalDateTime.now();

            User admin = userRepository.findByUsername(adminUsername)
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            User agent = userRepository.findByUsername(request.getAgentUsername())
                    .orElseThrow(() -> new RuntimeException("Agent not found"));

            Balance adminBalance = balanceRepository.findByUser(admin)
                    .orElseThrow(() -> new RuntimeException("Admin balance not found"));

            Balance agentBalance = balanceRepository.findByUser(agent)
                    .orElseThrow(() -> new RuntimeException("Agent balance not found"));

            if (adminBalance.getCurrentBalance().compareTo(request.getAmount()) < 0) {
                throw new RuntimeException("Insufficient admin balance");
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

    @Transactional
    public List<RechargeCard> generateRechargeCards(GenerateCardsRequest request) {
        List<RechargeCard> cards = new ArrayList<>();
        for (int i = 0; i < request.getQuantity(); i++) {
            RechargeCard card = new RechargeCard();
            card.setCode(generateUniqueCardCode());
            card.setValue(request.getCardValue());
            card.setUsed(false);
            cards.add(card);
        }
        return rechargeCardRepository.saveAll(cards);
    }

    private String generateUniqueCardCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        String code;

        do {
            StringBuilder sb = new StringBuilder(16);
            for (int i = 0; i < 16; i++) {
                sb.append(characters.charAt(random.nextInt(characters.length())));
            }
            code = sb.toString();
        } while (rechargeCardRepository.existsByCode(code));

        return code;
    }

    public List<CardStatisticsDTO> getCardStatistics() {
        return rechargeCardRepository.getCardStatistics();
    }

    public CardStatisticsDTO getCardStatisticsByValue(BigDecimal value) {
        return rechargeCardRepository.getCardStatisticsByValue(value);
    }

    public CardStatisticsDTO getOverallCardStatistics() {
        return rechargeCardRepository.getOverallCardStatistics();
    }
}