package com.example.userservice.service;

import com.example.userservice.dto.BalanceRechargeRequest;
import com.example.userservice.entity.Balance;
import com.example.userservice.entity.BalanceTransaction;
import com.example.userservice.entity.User;
import com.example.userservice.repository.BalanceRepository;
import com.example.userservice.repository.BalanceTransactionRepository;
import com.example.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceTransactionRepository transactionRepository;

    @Transactional
    public String rechargePOSBalance(BalanceRechargeRequest request) {
        User agent = userRepository.findByUsername(request.getAgentUsername())
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        if (!agent.getPasswordHash().equals(request.getAgentPassword())) {
            throw new RuntimeException("Invalid password");
        }

        User pos = userRepository.findByUsername(request.getPosUsername())
                .orElseThrow(() -> new RuntimeException("POS user not found"));

        Balance agentBalance = balanceRepository.findByUser(agent)
                .orElseThrow(() -> new RuntimeException("Agent balance not found"));

        Balance posBalance = balanceRepository.findByUser(pos)
                .orElseThrow(() -> new RuntimeException("POS balance not found"));

        BigDecimal amount = request.getAmount();

        if (agentBalance.getCurrentBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient agent balance");
        }

        // Update balances
        agentBalance.setCurrentBalance(agentBalance.getCurrentBalance().subtract(amount));
        posBalance.setCurrentBalance(posBalance.getCurrentBalance().add(amount));

        balanceRepository.save(agentBalance);
        balanceRepository.save(posBalance);

        // Log transaction
        BalanceTransaction transaction = new BalanceTransaction();
        transaction.setSource(agent);
        transaction.setDestination(pos);
        transaction.setAmount(amount);
        transaction.setTransactionType("recharge");
        transaction.setCreatedBy(agent);

        transactionRepository.save(transaction);

        return "Recharge successful";
    }
}
