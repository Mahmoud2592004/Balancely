package com.example.userservice.controller;

import com.example.userservice.dto.PerformanceMetricsDTO;
import com.example.userservice.entity.BalanceTransaction;
import com.example.userservice.service.TechnicalExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/technical")
public class TechnicalExpertController {

    private final TechnicalExpertService technicalExpertService;

    @Autowired
    public TechnicalExpertController(TechnicalExpertService technicalExpertService) {
        this.technicalExpertService = technicalExpertService;
    }

    @GetMapping("/failed-transactions")
    public ResponseEntity<List<BalanceTransaction>> getFailedTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds) { // Changed to List<Long>

        return ResponseEntity.ok(
                technicalExpertService.getFailedTransactions(startDate, endDate, locationIds)
        );
    }

    @GetMapping("/average-time")
    public ResponseEntity<Double> getAverageExecutionTime(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds) { // Changed to List<Long>

        return ResponseEntity.ok(
                technicalExpertService.getAverageExecutionTime(startDate, endDate, locationIds)
        );
    }

    @GetMapping("/metrics/daily")
    public ResponseEntity<List<PerformanceMetricsDTO>> getDailyMetrics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds) { // Changed to List<Long>

        return ResponseEntity.ok(
                technicalExpertService.getDailyMetrics(startDate, endDate, locationIds)
        );
    }

    @GetMapping("/metrics/weekly")
    public ResponseEntity<List<PerformanceMetricsDTO>> getWeeklyMetrics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds) { // Changed to List<Long>

        return ResponseEntity.ok(
                technicalExpertService.getWeeklyMetrics(startDate, endDate, locationIds)
        );
    }

    @GetMapping("/metrics/monthly")
    public ResponseEntity<List<PerformanceMetricsDTO>> getMonthlyMetrics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds) { // Changed to List<Long>

        return ResponseEntity.ok(
                technicalExpertService.getMonthlyMetrics(startDate, endDate, locationIds)
        );
    }
}