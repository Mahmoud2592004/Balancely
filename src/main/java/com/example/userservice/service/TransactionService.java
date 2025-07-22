package com.example.userservice.service;

import com.example.userservice.entity.BalanceTransaction;
import com.example.userservice.repository.BalanceTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final BalanceTransactionRepository transactionRepository;

    @Async
    public void logTransaction(BalanceTransaction transaction) {
        transactionRepository.save(transaction);
    }
}