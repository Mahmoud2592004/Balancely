package com.example.userservice.service;

import com.example.userservice.dto.BalanceRechargeRequest;
import com.example.userservice.entity.*;
import com.example.userservice.exception.*;
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
        validateBalanceRechargeRequest(request);

        LocalDateTime transactionStart = LocalDateTime.now();
        BalanceTransaction transaction = new BalanceTransaction();
        transaction.setTransactionType("recharge");
        transaction.setAmount(request.getAmount());
        transaction.setStartTime(transactionStart);
        transaction.setStatus("PENDING");

        try {
            User agent = userRepository.findByUsername(agentUsername)
                    .orElseThrow(() -> new UserNotFoundException("Agent with username '" + agentUsername + "' not found"));

            User pos = userRepository.findByUsername(request.getPosUsername())
                    .orElseThrow(() -> new UserNotFoundException("POS user with username '" + request.getPosUsername() + "' not found"));

            Balance agentBalance = balanceRepository.findByUser(agent)
                    .orElseThrow(() -> new ResourceNotFoundException("AGENT_BALANCE_NOT_FOUND", "Balance for agent not found"));

            Balance posBalance = balanceRepository.findByUser(pos)
                    .orElseThrow(() -> new ResourceNotFoundException("POS_BALANCE_NOT_FOUND", "Balance for POS user not found"));

            if (agentBalance.getCurrentBalance().compareTo(request.getAmount()) < 0) {
                throw new InsufficientBalanceException("INSUFFICIENT_AGENT_BALANCE", "Agent has insufficient balance to perform recharge");
            }

            agentBalance.setCurrentBalance(agentBalance.getCurrentBalance().subtract(request.getAmount()));
            posBalance.setCurrentBalance(posBalance.getCurrentBalance().add(request.getAmount()));
            balanceRepository.save(agentBalance);
            balanceRepository.save(posBalance);

            transaction.setSource(agent);
            transaction.setDestination(pos);
            transaction.setUser(agent); // Set the user field to the agent
            transaction.setStatus("SUCCESS");
            transaction.setEndTime(LocalDateTime.now());
            // Execution time is calculated in @PostPersist, no need to set here
            transactionRepository.save(transaction);

            return "Recharge successful. POS balance increased by " + request.getAmount();
        } catch (Exception ex) {
            transaction.setStatus("FAILED");
            transaction.setEndTime(LocalDateTime.now());
            transactionRepository.save(transaction);
            throw ex;
        }
    }

    private void validateBalanceRechargeRequest(BalanceRechargeRequest request) {
        if (request.getPosUsername() == null || request.getPosUsername().isBlank()) {
            throw new ValidationException("INVALID_POS_USERNAME", "POS username cannot be empty");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("INVALID_AMOUNT", "Recharge amount must be greater than zero");
        }
    }
}