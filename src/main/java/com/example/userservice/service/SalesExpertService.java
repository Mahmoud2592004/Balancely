package com.example.userservice.service;

import com.example.userservice.dto.SalesStatisticsDTO;
import com.example.userservice.repository.SalesExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesExpertService {

    private final SalesExpertRepository salesExpertRepository;

    @Autowired
    public SalesExpertService(SalesExpertRepository salesExpertRepository) {
        this.salesExpertRepository = salesExpertRepository;
    }

    public List<SalesStatisticsDTO> getDailySales(LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        List<Object[]> rawData = salesExpertRepository.getDailySalesData(
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end
        );
        return transformData(rawData);
    }

    public List<SalesStatisticsDTO> getWeeklySales(LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        List<Object[]> rawData = salesExpertRepository.getWeeklySalesData(
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end
        );
        return transformData(rawData);
    }

    public List<SalesStatisticsDTO> getMonthlySales(LocalDate startDate, LocalDate endDate, List<Long> locationIds) {
        LocalDateTime start = getStartDateTime(startDate);
        LocalDateTime end = getEndDateTime(endDate);
        List<Object[]> rawData = salesExpertRepository.getMonthlySalesData(
                locationIds != null && !locationIds.isEmpty() ? locationIds : null,
                start, end
        );
        return transformData(rawData);
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

            // Calculate profit: 0.5 LE per card + 0.25 LE per recharge
            BigDecimal profit = BigDecimal.valueOf(cardCount * 0.5 + rechargeCount * 0.25);

            return new SalesStatisticsDTO(period, totalTransactions, totalSales, profit);
        }).collect(Collectors.toList());
    }
}