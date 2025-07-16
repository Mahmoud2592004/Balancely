package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.entity.RechargeCard;
import com.example.userservice.entity.Transaction;
import com.example.userservice.exception.UserNotFoundException;
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

    private final SmsService smsService;

    public UserService(UserRepository userRepository,
                       RechargeCardRepository rechargeCardRepository,
                       TransactionRepository transactionRepository,
                       SmsService smsService) {
        this.userRepository = userRepository;
        this.rechargeCardRepository = rechargeCardRepository;
        this.transactionRepository = transactionRepository;
        this.smsService = smsService;
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
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
    }

    // Create new user
    public User createUser(User user) {
        user.setBalance(user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO);
        User savedUser = userRepository.save(user);

        if (savedUser.getRole() != null && "POS".equalsIgnoreCase(savedUser.getRole().getName())) {
            // Simulate sending SMS
            System.out.println("SMS sent to " + savedUser.getPhoneNumber() + ": Welcome! You are now working with us as a POS.");
        }

        return savedUser;
    }



    // Recharge user using card
    public Transaction rechargeUsingCard(Long userId, String posUsername, String posPassword, String code) {
        RechargeCard card = rechargeCardRepository.findByCode(code);

        if (card == null || Boolean.TRUE.equals(card.getIsUsed())) {
            throw new IllegalArgumentException("Invalid or already used card.");
        }

        // Get POS (salesman) by username
        User pos = userRepository.findByUsername(posUsername);
        if (pos == null) {
            throw new IllegalArgumentException("POS user not found.");
        }

        // Check password (plaintext comparison here – you can hash later if needed)
        if (!pos.getPasswordHash().equals(posPassword)) {
            throw new IllegalArgumentException("Incorrect POS credentials.");
        }

        // Get target user
        User user = getUserById(userId);

        // Check POS balance before deducting
        if (pos.getBalance().compareTo(card.getValue()) < 0) {
            throw new IllegalArgumentException("POS has insufficient balance.");
        }

        // Transfer balance
        user.setBalance(user.getBalance().add(card.getValue()));
        pos.setBalance(pos.getBalance().subtract(card.getValue()));
        userRepository.save(user);
        userRepository.save(pos);

        // Mark card as used
        card.setIsUsed(true);
        card.setUsedBy(userId);
        card.setUsedAt(LocalDateTime.now());
        rechargeCardRepository.save(card);

        // Record transaction
        Transaction txn = new Transaction();
        txn.setRequesterId(pos.getId()); // POS (seller)
        txn.setTargetId(userId);         // Buyer
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
