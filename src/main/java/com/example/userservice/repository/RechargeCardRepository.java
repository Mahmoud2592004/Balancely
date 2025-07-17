package com.example.userservice.repository;

import com.example.userservice.entity.RechargeCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface RechargeCardRepository extends JpaRepository<RechargeCard, Long> {
    List<RechargeCard> findByUsedById(Long userId);
    List<RechargeCard> findByIsUsedFalse();
    List<RechargeCard> findByIsUsedFalseAndValue(BigDecimal value);

}
