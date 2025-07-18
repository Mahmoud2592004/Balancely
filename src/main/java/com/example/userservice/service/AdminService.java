package com.example.userservice.service;

import com.example.userservice.dto.RechargeAgentRequest;
import com.example.userservice.entity.Balance;
import com.example.userservice.entity.BalanceTransaction;
import com.example.userservice.entity.User;
import com.example.userservice.repository.BalanceRepository;
import com.example.userservice.repository.BalanceTransactionRepository;
import com.example.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceTransactionRepository transactionRepository;

    @Transactional
    public String rechargeAgentBalance(RechargeAgentRequest request) {
        BalanceTransaction transaction = new BalanceTransaction();
        transaction.setTransactionType("admin-recharge");
        transaction.setAmount(request.getAmount());
        transaction.setStartTime(LocalDateTime.now());
        transaction.setStatus("PENDING");

        try {
            LocalDateTime processStart = LocalDateTime.now();

            User admin = userRepository.findByUsername(request.getAdminUsername())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));

            if (!admin.getPasswordHash().equals(request.getAdminPassword())) {
                throw new RuntimeException("Invalid admin password");
            }

            User agent = userRepository.findByUsername(request.getAgentUsername())
                    .orElseThrow(() -> new RuntimeException("Agent not found"));

            Balance adminBalance = balanceRepository.findByUser(admin)
                    .orElseThrow(() -> new RuntimeException("Admin balance not found"));

            Balance agentBalance = balanceRepository.findByUser(agent)
                    .orElseThrow(() -> new RuntimeException("Agent balance not found"));

            if (adminBalance.getCurrentBalance().compareTo(request.getAmount()) < 0) {
                throw new RuntimeException("Insufficient admin balance");
            }

            // Process transaction
            adminBalance.setCurrentBalance(adminBalance.getCurrentBalance().subtract(request.getAmount()));
            agentBalance.setCurrentBalance(agentBalance.getCurrentBalance().add(request.getAmount()));
            balanceRepository.save(adminBalance);
            balanceRepository.save(agentBalance);

            transaction.setSource(admin);
            transaction.setDestination(agent);
            transaction.setStatus("SUCCESS");
            transaction.setEndTime(LocalDateTime.now());
            transaction.setExecutionTime(Duration.between(processStart, transaction.getEndTime()).toMillis());

            transactionRepository.save(transaction); // ✅ Save after setting required fields

            return "Recharge successful. Agent balance increased by " + request.getAmount();
        } catch (Exception ex) {
            // Handle error case and set source/destination if available
            try {
                User admin = userRepository.findByUsername(request.getAdminUsername()).orElse(null);
                User agent = userRepository.findByUsername(request.getAgentUsername()).orElse(null);
                if (admin != null) transaction.setSource(admin);
                if (agent != null) transaction.setDestination(agent);
            } catch (Exception ignored) {}

            transaction.setStatus("FAILED");
            transaction.setEndTime(LocalDateTime.now());
            if (transaction.getStartTime() != null) {
                transaction.setExecutionTime(Duration.between(transaction.getStartTime(), transaction.getEndTime()).toMillis());
            }
            transactionRepository.save(transaction);
            throw ex;
        }
    }
}