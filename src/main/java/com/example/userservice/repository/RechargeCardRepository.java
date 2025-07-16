package com.example.userservice.repository;

import com.example.userservice.entity.RechargeCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RechargeCardRepository extends JpaRepository<RechargeCard, Long> {
    RechargeCard findByCode(String code);
}
