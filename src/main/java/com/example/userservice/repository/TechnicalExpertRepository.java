package com.example.userservice.repository;

import com.example.userservice.dto.PerformanceMetricsDTO;
import com.example.userservice.entity.BalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface TechnicalExpertRepository extends JpaRepository<BalanceTransaction, Long> {

    @Query("SELECT bt FROM BalanceTransaction bt " +
            "JOIN bt.source s " +
            "JOIN s.location l " +
            "WHERE bt.status = 'FAILED' " +
            "AND (:locationId IS NULL OR l.id = :locationId) " +
            "AND bt.timestamp BETWEEN :startDate AND :endDate")
    List<BalanceTransaction> findFailedTransactions(
            @Param("locationId") Long locationId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT AVG(bt.executionTime) FROM BalanceTransaction bt " +
            "JOIN bt.source s " +
            "JOIN s.location l " +
            "WHERE bt.executionTime IS NOT NULL " +
            "AND (:locationId IS NULL OR l.id = :locationId) " +
            "AND bt.timestamp BETWEEN :startDate AND :endDate")
    Double findAverageExecutionTime(
            @Param("locationId") Long locationId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT NEW com.example.userservice.dto.PerformanceMetricsDTO( " +
            "FUNCTION('DATE', bt.timestamp), " +
            "COUNT(bt), " +
            "SUM(CASE WHEN bt.status = 'FAILED' THEN 1 ELSE 0 END), " +
            "AVG(bt.executionTime)) " +
            "FROM BalanceTransaction bt " +
            "JOIN bt.source s " +
            "JOIN s.location l " +
            "WHERE (:locationId IS NULL OR l.id = :locationId) " +
            "AND bt.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', bt.timestamp) " +
            "ORDER BY FUNCTION('DATE', bt.timestamp) DESC")
    List<PerformanceMetricsDTO> getDailyMetrics(
            @Param("locationId") Long locationId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT NEW com.example.userservice.dto.PerformanceMetricsDTO( " +
            "FUNCTION('WEEK', bt.timestamp), " +
            "COUNT(bt), " +
            "SUM(CASE WHEN bt.status = 'FAILED' THEN 1 ELSE 0 END), " +
            "AVG(bt.executionTime)) " +
            "FROM BalanceTransaction bt " +
            "JOIN bt.source s " +
            "JOIN s.location l " +
            "WHERE (:locationId IS NULL OR l.id = :locationId) " +
            "AND bt.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('WEEK', bt.timestamp) " +
            "ORDER BY FUNCTION('WEEK', bt.timestamp) DESC")
    List<PerformanceMetricsDTO> getWeeklyMetrics(
            @Param("locationId") Long locationId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT NEW com.example.userservice.dto.PerformanceMetricsDTO( " +
            "FUNCTION('MONTH', bt.timestamp), " +
            "COUNT(bt), " +
            "SUM(CASE WHEN bt.status = 'FAILED' THEN 1 ELSE 0 END), " +
            "AVG(bt.executionTime)) " +
            "FROM BalanceTransaction bt " +
            "JOIN bt.source s " +
            "JOIN s.location l " +
            "WHERE (:locationId IS NULL OR l.id = :locationId) " +
            "AND bt.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('MONTH', bt.timestamp) " +
            "ORDER BY FUNCTION('MONTH', bt.timestamp) DESC")
    List<PerformanceMetricsDTO> getMonthlyMetrics(
            @Param("locationId") Long locationId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}