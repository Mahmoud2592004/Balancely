package com.example.userservice.repository;

import com.example.userservice.entity.BalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface SalesExpertRepository extends JpaRepository<BalanceTransaction, Long> {

    @Query("SELECT FUNCTION('DATE', bt.timestamp) AS period, " +
            "COUNT(bt) AS totalTransactions, " +
            "SUM(bt.amount) AS totalSales, " +
            "SUM(CASE WHEN bt.transactionType = 'card' THEN 1 ELSE 0 END) AS cardCount, " +
            "SUM(CASE WHEN bt.transactionType = 'recharge' THEN 1 ELSE 0 END) AS rechargeCount " +
            "FROM BalanceTransaction bt " +
            "JOIN bt.source s " +
            "JOIN s.location l " +
            "WHERE bt.transactionType NOT IN ('admin-recharge') " +
            "AND (COALESCE(:locationIds) IS NULL OR l.id IN :locationIds) " + // Changed to IN clause
            "AND bt.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DATE', bt.timestamp) " +
            "ORDER BY FUNCTION('DATE', bt.timestamp) DESC")
    List<Object[]> getDailySalesData(
            @Param("locationIds") List<Long> locationIds, // Changed to List<Long>
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT EXTRACT(WEEK FROM bt.timestamp) AS period, " +
            "COUNT(bt) AS totalTransactions, " +
            "SUM(bt.amount) AS totalSales, " +
            "SUM(CASE WHEN bt.transactionType = 'card' THEN 1 ELSE 0 END) AS cardCount, " +
            "SUM(CASE WHEN bt.transactionType = 'recharge' THEN 1 ELSE 0 END) AS rechargeCount " +
            "FROM BalanceTransaction bt " +
            "JOIN bt.source s " +
            "JOIN s.location l " +
            "WHERE bt.transactionType NOT IN ('admin-recharge') " +
            "AND (COALESCE(:locationIds) IS NULL OR l.id IN :locationIds) " + // Changed to IN clause
            "AND bt.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY EXTRACT(WEEK FROM bt.timestamp) " +
            "ORDER BY EXTRACT(WEEK FROM bt.timestamp) DESC")
    List<Object[]> getWeeklySalesData(
            @Param("locationIds") List<Long> locationIds, // Changed to List<Long>
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT EXTRACT(MONTH FROM bt.timestamp) AS period, " +
            "COUNT(bt) AS totalTransactions, " +
            "SUM(bt.amount) AS totalSales, " +
            "SUM(CASE WHEN bt.transactionType = 'card' THEN 1 ELSE 0 END) AS cardCount, " +
            "SUM(CASE WHEN bt.transactionType = 'recharge' THEN 1 ELSE 0 END) AS rechargeCount " +
            "FROM BalanceTransaction bt " +
            "JOIN bt.source s " +
            "JOIN s.location l " +
            "WHERE bt.transactionType NOT IN ('admin-recharge') " +
            "AND (COALESCE(:locationIds) IS NULL OR l.id IN :locationIds) " + // Changed to IN clause
            "AND bt.timestamp BETWEEN :startDate AND :endDate " +
            "GROUP BY EXTRACT(MONTH FROM bt.timestamp) " +
            "ORDER BY EXTRACT(MONTH FROM bt.timestamp) DESC")
    List<Object[]> getMonthlySalesData(
            @Param("locationIds") List<Long> locationIds, // Changed to List<Long>
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}