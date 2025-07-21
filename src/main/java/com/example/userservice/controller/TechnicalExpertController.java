package com.example.userservice.controller;

import com.example.userservice.dto.PerformanceMetricsDTO;
import com.example.userservice.entity.BalanceTransaction;
import com.example.userservice.service.TechnicalExpertService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/technical")
@RequiredArgsConstructor
public class TechnicalExpertController {

    private final TechnicalExpertService technicalExpertService;

    @GetMapping("/failed-transactions")
    public ResponseEntity<List<BalanceTransaction>> getFailedTransactions(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        String username = authentication.getName();
        return ResponseEntity.ok(
                technicalExpertService.getFailedTransactions(username, startDate, endDate, locationIds, page, size)
        );
    }

    @GetMapping("/average-time")
    public ResponseEntity<Double> getAverageExecutionTime(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds) {

        String username = authentication.getName();
        return ResponseEntity.ok(
                technicalExpertService.getAverageExecutionTime(username, startDate, endDate, locationIds)
        );
    }

    @GetMapping("/metrics/daily")
    public ResponseEntity<List<PerformanceMetricsDTO>> getDailyMetrics(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds) {

        String username = authentication.getName();
        return ResponseEntity.ok(
                technicalExpertService.getDailyMetrics(username, startDate, endDate, locationIds)
        );
    }

    @GetMapping("/metrics/weekly")
    public ResponseEntity<List<PerformanceMetricsDTO>> getWeeklyMetrics(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds) {

        String username = authentication.getName();
        return ResponseEntity.ok(
                technicalExpertService.getWeeklyMetrics(username, startDate, endDate, locationIds)
        );
    }

    @GetMapping("/metrics/monthly")
    public ResponseEntity<List<PerformanceMetricsDTO>> getMonthlyMetrics(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds) {

        String username = authentication.getName();
        return ResponseEntity.ok(
                technicalExpertService.getMonthlyMetrics(username, startDate, endDate, locationIds)
        );
    }
}