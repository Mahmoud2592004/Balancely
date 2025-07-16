package com.example.userservice.repository;

import com.example.userservice.entity.PerformanceMetrics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PerformanceMetricsRepository extends JpaRepository<PerformanceMetrics, Long> {
    List<PerformanceMetrics> findByDate(LocalDate date);
    List<PerformanceMetrics> findByLocationId(Integer locationId);
    List<PerformanceMetrics> findByDateAndLocationId(LocalDate date, Integer locationId);
}
