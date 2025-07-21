package com.example.userservice.service;

import com.example.userservice.dto.PerformanceMetricsDTO;
import com.example.userservice.entity.BalanceTransaction;
import com.example.userservice.repository.TechnicalExpertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicalExpertService {
    private final TechnicalExpertRepository technicalExpertRepository;

    public List<BalanceTransaction> getFailedTransactions(String username, LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        return technicalExpertRepository.findFailedTransactions(
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end
        );
    }

    public Double getAverageExecutionTime(String username, LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        return technicalExpertRepository.findAverageExecutionTime(
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end
        );
    }

    public List<PerformanceMetricsDTO> getDailyMetrics(String username, LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        return technicalExpertRepository.getDailyMetrics(
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end
        );
    }

    public List<PerformanceMetricsDTO> getWeeklyMetrics(String username, LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        return technicalExpertRepository.getWeeklyMetrics(
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end
        );
    }

    public List<PerformanceMetricsDTO> getMonthlyMetrics(String username, LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        return technicalExpertRepository.getMonthlyMetrics(
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end
        );
    }

    private LocalDateTime getStartDateTime(LocalDate startDate) {
        return startDate != null ? startDate.atStartOfDay() : LocalDateTime.now().minusMonths(1);
    }

    private LocalDateTime getEndDateTime(LocalDate endDate) {
        return endDate != null ? endDate.atTime(LocalTime.MAX) : LocalDateTime.now();
    }
}