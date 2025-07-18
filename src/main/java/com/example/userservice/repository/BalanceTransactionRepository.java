package com.example.userservice.repository;

import com.example.userservice.entity.BalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction, Long> {
    List<BalanceTransaction> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
