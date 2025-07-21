package com.example.userservice.controller;

import com.example.userservice.dto.SalesStatisticsDTO;
import com.example.userservice.service.SalesExpertService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SalesExpertController {

    private final SalesExpertService salesExpertService;

    @GetMapping("/daily")
    public ResponseEntity<List<SalesStatisticsDTO>> getDailySales(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds) {

        String username = authentication.getName();
        return ResponseEntity.ok(
                salesExpertService.getDailySales(username, startDate, endDate, locationIds)
        );
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<SalesStatisticsDTO>> getWeeklySales(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds) {

        String username = authentication.getName();
        return ResponseEntity.ok(
                salesExpertService.getWeeklySales(username, startDate, endDate, locationIds)
        );
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<SalesStatisticsDTO>> getMonthlySales(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) List<Long> locationIds) {

        String username = authentication.getName();
        return ResponseEntity.ok(
                salesExpertService.getMonthlySales(username, startDate, endDate, locationIds)
        );
    }
}