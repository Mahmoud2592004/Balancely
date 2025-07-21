package com.example.userservice.service;

import com.example.userservice.entity.Balance;
import com.example.userservice.entity.User;
import com.example.userservice.repository.BalanceRepository;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;

    public Balance getBalance(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return balanceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Balance not found for user: " + username));
    }
}