package com.example.userservice.controller;

import com.example.userservice.dto.SalesStatisticsDTO;
import com.example.userservice.service.SalesExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sales")
public class SalesExpertController {

    private final SalesExpertService salesExpertService;

    @Autowired
    public SalesExpertController(SalesExpertService salesExpertService) {
        this.salesExpertService = salesExpertService;
    }

    @GetMapping("/daily")
    public ResponseEntity<List<SalesStatisticsDTO>> getDailySales(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds) { // Changed to List<Long>

        return ResponseEntity.ok(
                salesExpertService.getDailySales(startDate, endDate, locationIds)
        );
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<SalesStatisticsDTO>> getWeeklySales(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds) { // Changed to List<Long>

        return ResponseEntity.ok(
                salesExpertService.getWeeklySales(startDate, endDate, locationIds)
        );
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<SalesStatisticsDTO>> getMonthlySales(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds) { // Changed to List<Long>

        return ResponseEntity.ok(
                salesExpertService.getMonthlySales(startDate, endDate, locationIds)
        );
    }
}