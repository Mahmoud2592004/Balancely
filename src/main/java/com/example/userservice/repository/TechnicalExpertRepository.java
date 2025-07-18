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
            "AND (COALESCE(:locationIds) IS NULL OR l.id IN :locationIds) " + // Changed to IN clause
            "AND bt.timestamp BETWEEN :startDate AND :endDate")
    List<BalanceTransaction> findFailedTransactions(
            @Param("locationIds") List<Long> locationIds, // Changed to List<Long>
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(AVG(bt.executionTime), 0) FROM BalanceTransaction bt " +
            "JOIN bt.source s " +
            "JOIN s.location l " +
            "WHERE bt.executionTime IS NOT NULL " +
            "AND bt.executionTime > 0 " +
            "AND (COALESCE(:locationIds) IS NULL OR l.id IN :locationIds) " + // Changed to IN clause
            "AND bt.timestamp BETWEEN :startDate AND :endDate")
    Double findAverageExecutionTime(
            @Param("locationIds") List<Long> locationIds, // Changed to List<Long>
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
            "WHERE (COALESCE(:locationIds) IS NULL OR l.id IN :locationIds) " + // Changed to IN clause
            "AND bt.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', bt.timestamp) " +
            "ORDER BY FUNCTION('DATE', bt.timestamp) DESC")
    List<PerformanceMetricsDTO> getDailyMetrics(
            @Param("locationIds") List<Long> locationIds, // Changed to List<Long>
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT NEW com.example.userservice.dto.PerformanceMetricsDTO( " +
            "EXTRACT(WEEK FROM bt.timestamp), " +
            "COUNT(bt), " +
            "SUM(CASE WHEN bt.status = 'FAILED' THEN 1 ELSE 0 END), " +
            "COALESCE(AVG(bt.executionTime), 0)) " +
            "FROM BalanceTransaction bt " +
            "JOIN bt.source s " +
            "JOIN s.location l " +
            "WHERE (COALESCE(:locationIds) IS NULL OR l.id IN :locationIds) " + // Changed to IN clause
            "AND bt.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY EXTRACT(WEEK FROM bt.timestamp) " +
            "ORDER BY EXTRACT(WEEK FROM bt.timestamp) DESC")
    List<PerformanceMetricsDTO> getWeeklyMetrics(
            @Param("locationIds") List<Long> locationIds, // Changed to List<Long>
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT NEW com.example.userservice.dto.PerformanceMetricsDTO( " +
            "EXTRACT(MONTH FROM bt.timestamp), " +
            "COUNT(bt), " +
            "SUM(CASE WHEN bt.status = 'FAILED' THEN 1 ELSE 0 END), " +
            "COALESCE(AVG(bt.executionTime), 0)) " +
            "FROM BalanceTransaction bt " +
            "JOIN bt.source s " +
            "JOIN s.location l " +
            "WHERE (COALESCE(:locationIds) IS NULL OR l.id IN :locationIds) " + // Changed to IN clause
            "AND bt.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY EXTRACT(MONTH FROM bt.timestamp) " +
            "ORDER BY EXTRACT(MONTH FROM bt.timestamp) DESC")
    List<PerformanceMetricsDTO> getMonthlyMetrics(
            @Param("locationIds") List<Long> locationIds, // Changed to List<Long>
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}