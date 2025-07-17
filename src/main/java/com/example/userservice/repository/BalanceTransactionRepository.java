package com.example.userservice.repository;

import com.example.userservice.entity.BalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction, Long> {}
