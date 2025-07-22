package com.example.userservice.repository;

import com.example.userservice.dto.CardStatisticsDTO;
import com.example.userservice.entity.RechargeCard;
import com.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface RechargeCardRepository extends JpaRepository<RechargeCard, Long> {
    List<RechargeCard> findByUsedById(Long userId);
    List<RechargeCard> findByIsUsedFalse();
    long countByIsUsedFalseAndValue(BigDecimal value);

    @Query("SELECT r FROM RechargeCard r WHERE r.isUsed = false AND r.value = :value")
    List<RechargeCard> findTopByIsUsedFalseAndValue(@Param("value") BigDecimal value, Pageable pageable);

    @Modifying
    @Query("UPDATE RechargeCard r SET r.isUsed = true, r.usedBy = :user, r.usedAt = :usedAt WHERE r.id IN :cardIds AND r.version = :version")
    int updateCardsToUsed(@Param("cardIds") List<Long> cardIds, @Param("user") User user, @Param("usedAt") LocalDateTime usedAt, @Param("version") Long version);

    boolean existsByCode(String code);

    @Query("SELECT NEW com.example.userservice.dto.CardStatisticsDTO(" +
            "r.value, COUNT(r), SUM(CASE WHEN r.isUsed = true THEN 1 ELSE 0 END)) " +
            "FROM RechargeCard r " +
            "GROUP BY r.value")
    List<CardStatisticsDTO> getCardStatistics();

    @Query("SELECT NEW com.example.userservice.dto.CardStatisticsDTO(" +
            "r.value, COUNT(r), SUM(CASE WHEN r.isUsed = true THEN 1 ELSE 0 END)) " +
            "FROM RechargeCard r " +
            "WHERE r.value = :value " +
            "GROUP BY r.value")
    CardStatisticsDTO getCardStatisticsByValue(@Param("value") BigDecimal value);

    @Query("SELECT NEW com.example.userservice.dto.CardStatisticsDTO(" +
            "COUNT(r), SUM(CASE WHEN r.isUsed = true THEN 1 ELSE 0 END)) " +
            "FROM RechargeCard r")
    CardStatisticsDTO getOverallCardStatistics();

    @Query("SELECT NEW com.example.userservice.dto.CardStatisticsDTO(" +
            "r.value, COUNT(r), SUM(CASE WHEN r.isUsed = true THEN 1 ELSE 0 END)) " +
            "FROM RechargeCard r " +
            "WHERE r.usedBy.id = :userId " +
            "GROUP BY r.value")
    List<CardStatisticsDTO> findCardStatisticsByUser(@Param("userId") Long userId);

    @Query("SELECT NEW com.example.userservice.dto.CardStatisticsDTO(" +
            "r.value, COUNT(r), SUM(CASE WHEN r.isUsed = true THEN 1 ELSE 0 END)) " +
            "FROM RechargeCard r " +
            "WHERE r.usedBy.id = :userId AND r.value = :value " +
            "GROUP BY r.value")
    CardStatisticsDTO findCardStatisticsByUserAndValue(
            @Param("userId") Long userId,
            @Param("value") BigDecimal value);

    @Query("SELECT NEW com.example.userservice.dto.CardStatisticsDTO(" +
            "COUNT(r), SUM(CASE WHEN r.isUsed = true THEN 1 ELSE 0 END)) " +
            "FROM RechargeCard r " +
            "WHERE r.usedBy.id = :userId")
    CardStatisticsDTO findOverallCardStatisticsByUser(@Param("userId") Long userId);
}