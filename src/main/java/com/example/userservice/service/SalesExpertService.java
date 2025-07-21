package com.example.userservice.service;

import com.example.userservice.dto.SalesStatisticsDTO;
import com.example.userservice.exception.ValidationException;
import com.example.userservice.repository.SalesExpertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesExpertService {
    private final SalesExpertRepository salesExpertRepository;

    public List<SalesStatisticsDTO> getDailySales(String username, LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        validateSalesRequest(username, startDate, endDate, locationIds);
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        List<Object[]> rawData = salesExpertRepository.getDailySalesData(
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end
        );
        return transformData(rawData);
    }

    public List<SalesStatisticsDTO> getWeeklySales(String username, LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        validateSalesRequest(username, startDate, endDate, locationIds);
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        List<Object[]> rawData = salesExpertRepository.getWeeklySalesData(
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end
        );
        return transformData(rawData);
    }

    public List<SalesStatisticsDTO> getMonthlySales(String username, LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        validateSalesRequest(username, startDate, endDate, locationIds);
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        List<Object[]> rawData = salesExpertRepository.getMonthlySalesData(
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end
        );
        return transformData(rawData);
    }

    private void validateSalesRequest(String username, LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("INVALID_USERNAME", "Username cannot be empty");
        }
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new ValidationException("INVALID_DATE_RANGE", "Start date cannot be after end date");
        }
        if (locationIds != null && locationIds.stream().anyMatch(id -> id == null || id <= 0)) {
            throw new ValidationException("INVALID_LOCATION_IDS", "Location IDs must be valid positive numbers");
        }
    }

    private LocalDateTime getStartDateTime(LocalDate startDate) {
        return startDate != null ? startDate.atStartOfDay() : LocalDateTime.now().minusMonths(1);
    }

    private LocalDateTime getEndDateTime(LocalDate endDate) {
        return endDate != null ? endDate.atTime(LocalTime.MAX) : LocalDateTime.now();
    }

    private List<SalesStatisticsDTO> transformData(List<Object[]> rawData) {
        return rawData.stream().map(row -> {
            Object period = row[0];
            Long totalTransactions = (Long) row[1];
            BigDecimal totalSales = (BigDecimal) row[2];
            Long cardCount = (Long) row[3];
            Long rechargeCount = (Long) row[4];
            BigDecimal profit = BigDecimal.valueOf(cardCount * 0.5 + rechargeCount * 0.25);
            return new SalesStatisticsDTO(period, totalTransactions, totalSales, profit);
        }).collect(Collectors.toList());
    }
}