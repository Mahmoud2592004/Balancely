package com.example.userservice.service;

import com.example.userservice.dto.PerformanceMetricsDTO;
import com.example.userservice.entity.BalanceTransaction;
import com.example.userservice.repository.TechnicalExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TechnicalExpertService {

    private final TechnicalExpertRepository technicalExpertRepository;

    @Autowired
    public TechnicalExpertService(TechnicalExpertRepository technicalExpertRepository) {
        this.technicalExpertRepository = technicalExpertRepository;
    }

    public List<BalanceTransaction> getFailedTransactions(LocalDate startDate, LocalDate endDate, Long locationId) {
        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = endDate != null ? endDate.atTime(LocalTime.MAX) : LocalDateTime.now();
        return technicalExpertRepository.findFailedTransactions(locationId, start, end);
    }

    public Double getAverageExecutionTime(LocalDate startDate, LocalDate endDate, Long locationId) {
        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = endDate != null ? endDate.atTime(LocalTime.MAX) : LocalDateTime.now();
        return technicalExpertRepository.findAverageExecutionTime(locationId, start, end);
    }

    public List<PerformanceMetricsDTO> getDailyMetrics(LocalDate startDate, LocalDate endDate, Long locationId) {
        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = endDate != null ? endDate.atTime(LocalTime.MAX) : LocalDateTime.now();
        return technicalExpertRepository.getDailyMetrics(locationId, start, end);
    }

    public List<PerformanceMetricsDTO> getWeeklyMetrics(LocalDate startDate, LocalDate endDate, Long locationId) {
        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = endDate != null ? endDate.atTime(LocalTime.MAX) : LocalDateTime.now();
        return technicalExpertRepository.getWeeklyMetrics(locationId, start, end);
    }

    public List<PerformanceMetricsDTO> getMonthlyMetrics(LocalDate startDate, LocalDate endDate, Long locationId) {
        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = endDate != null ? endDate.atTime(LocalTime.MAX) : LocalDateTime.now();
        return technicalExpertRepository.getMonthlyMetrics(locationId, start, end);
    }
}