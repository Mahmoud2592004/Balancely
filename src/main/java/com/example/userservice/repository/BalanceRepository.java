package com.example.userservice.repository;

import com.example.userservice.entity.Balance;
import com.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findByUser(User user);
    Optional<Balance> findByUserId(Long userId);
}
