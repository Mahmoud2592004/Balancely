package com.example.userservice.service;

import com.example.userservice.dto.BalanceRechargeRequest;
import com.example.userservice.entity.*;
import com.example.userservice.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AgentService {
    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceTransactionRepository transactionRepository;

    @Transactional
    public String rechargePOSBalance(String agentUsername, BalanceRechargeRequest request) {
        LocalDateTime transactionStart = LocalDateTime.now();
        BalanceTransaction transaction = new BalanceTransaction();
        transaction.setTransactionType("recharge");
        transaction.setAmount(request.getAmount());
        transaction.setStartTime(transactionStart);
        transaction.setStatus("PENDING");

        try {
            User agent = userRepository.findByUsername(agentUsername)
                    .orElseThrow(() -> new RuntimeException("Agent not found"));

            User pos = userRepository.findByUsername(request.getPosUsername())
                    .orElseThrow(() -> new RuntimeException("POS user not found"));

            Balance agentBalance = balanceRepository.findByUser(agent)
                    .orElseThrow(() -> new RuntimeException("Agent balance not found"));

            Balance posBalance = balanceRepository.findByUser(pos)
                    .orElseThrow(() -> new RuntimeException("POS balance not found"));

            if (agentBalance.getCurrentBalance().compareTo(request.getAmount()) < 0) {
                throw new RuntimeException("Insufficient agent balance");
            }

            agentBalance.setCurrentBalance(agentBalance.getCurrentBalance().subtract(request.getAmount()));
            posBalance.setCurrentBalance(posBalance.getCurrentBalance().add(request.getAmount()));
            balanceRepository.save(agentBalance);
            balanceRepository.save(posBalance);

            transaction.setSource(agent);
            transaction.setDestination(pos);
            transaction.setStatus("SUCCESS");
            transaction.setEndTime(LocalDateTime.now());
            transaction.setExecutionTime(Duration.between(transactionStart, transaction.getEndTime()).toMillis());
            transactionRepository.save(transaction);

            return "Recharge successful. POS balance increased by " + request.getAmount();
        } catch (Exception ex) {
            transaction.setStatus("FAILED");
            transaction.setEndTime(LocalDateTime.now());
            transaction.setExecutionTime(Duration.between(transactionStart, transaction.getEndTime()).toMillis());
            transactionRepository.save(transaction);
            throw ex;
        }
    }
}