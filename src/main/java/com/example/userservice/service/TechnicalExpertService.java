package com.example.userservice.service;

import com.example.userservice.dto.PerformanceMetricsDTO;
import com.example.userservice.entity.BalanceTransaction;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.exception.ValidationException;
import com.example.userservice.repository.TechnicalExpertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicalExpertService {
    private final TechnicalExpertRepository technicalExpertRepository;

    public List<BalanceTransaction> getFailedTransactions(String username, LocalDate startDate, LocalDate endDate, List<Long> locationIds, int page, int size) {
        validateTechnicalRequest(username, startDate, endDate, locationIds, page, size);
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        Pageable pageable = PageRequest.of(page, size);
        return technicalExpertRepository.findFailedTransactions(
                username,
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end, pageable
        );
    }

    public Double getAverageExecutionTime(String username, LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        validateTechnicalRequest(username, startDate, endDate, locationIds, 0, 0);
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        Double result = technicalExpertRepository.findAverageExecutionTime(
                username,
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end
        );
        if (result == null) {
            throw new ResourceNotFoundException("NO_DATA_FOUND", "No execution time data available for the specified criteria");
        }
        return result;
    }

    public List<PerformanceMetricsDTO> getDailyMetrics(String username, LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        validateTechnicalRequest(username, startDate, endDate, locationIds, 0, 0);
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        return technicalExpertRepository.getDailyMetrics(
                username,
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end
        );
    }

    public List<PerformanceMetricsDTO> getWeeklyMetrics(String username, LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        validateTechnicalRequest(username, startDate, endDate, locationIds, 0, 0);
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        return technicalExpertRepository.getWeeklyMetrics(
                username,
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end
        );
    }

    public List<PerformanceMetricsDTO> getMonthlyMetrics(String username, LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        validateTechnicalRequest(username, startDate, endDate, locationIds, 0, 0);
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        return technicalExpertRepository.getMonthlyMetrics(
                username,
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end
        );
    }

    private void validateTechnicalRequest(String username, LocalDate startDate, LocalDate endDate, List<Long> locationIds, int page, int size) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("INVALID_USERNAME", "Username cannot be empty");
        }
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ValidationException("INVALID_DATE_RANGE", "Start date cannot be after end date");
        }
        if (locationIds != null && locationIds.stream().anyMatch(id -> id == null || id <= 0)) {
            throw new ValidationException("INVALID_LOCATION_IDS", "Location IDs must be valid positive numbers");
        }
        if (page < 0) {
            throw new ValidationException("INVALID_PAGE", "Page number cannot be negative");
        }
        if (size <= 0) {
            throw new ValidationException("INVALID_SIZE", "Page size must be greater than zero");
        }
    }

    private LocalDateTime getStartDateTime(LocalDate startDate) {
        return startDate != null ? startDate.atStartOfDay() : LocalDateTime.now().minusMonths(1);
    }

    private LocalDateTime getEndDateTime(LocalDate endDate) {
        return endDate != null ? endDate.atTime(LocalTime.MAX) : LocalDateTime.now();
    }
}