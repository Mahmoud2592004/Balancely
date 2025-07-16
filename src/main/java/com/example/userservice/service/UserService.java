package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.entity.RechargeCard;
import com.example.userservice.entity.Transaction;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.repository.RechargeCardRepository;
import com.example.userservice.repository.TransactionRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RechargeCardRepository rechargeCardRepository;
    private final TransactionRepository transactionRepository;

    public UserService(UserRepository userRepository,
                       RechargeCardRepository rechargeCardRepository,
                       TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.rechargeCardRepository = rechargeCardRepository;
        this.transactionRepository = transactionRepository;
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Get user by ID
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // Create new user
    public User createUser(User user) {
        user.setBalance(user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO);
        return userRepository.save(user);
    }

    // Recharge user using card
    public Transaction rechargeUsingCard(Long userId, String code) {
        RechargeCard card = rechargeCardRepository.findByCode(code);

        if (card == null) {
            throw new IllegalArgumentException("Invalid card code.");
        }
        if (Boolean.TRUE.equals(card.getIsUsed())) {
            throw new IllegalArgumentException("This card has already been used.");
        }

        User user = getUserById(userId);

        // Update user's balance
        BigDecimal newBalance = user.getBalance().add(card.getValue());
        user.setBalance(newBalance);
        userRepository.save(user);

        // Update card as used
        card.setIsUsed(true);
        card.setUsedBy(userId);
        card.setUsedAt(LocalDateTime.now());
        rechargeCardRepository.save(card);

        // Save transaction
        Transaction txn = new Transaction();
        txn.setRequesterId(-1L);
        txn.setTargetId(userId);
        txn.setChannel("card");
        txn.setTransactionType("recharge");
        txn.setAmount(card.getValue());
        txn.setStatus("success");
        txn.setRechargeCardId(card.getId());
        txn.setTimestamp(LocalDateTime.now());
        txn.setIsFraud(false);
        txn.setFraudScore(0.0);

        return transactionRepository.save(txn);
    }

    // Get user's balance
    public BigDecimal getUserBalance(Long userId) {
        User user = getUserById(userId);
        return user.getBalance();
    }

    // Get user's transaction history
    public List<Transaction> getTransactionHistory(Long userId) {
        return transactionRepository.findByTargetId(userId);
    }
}
