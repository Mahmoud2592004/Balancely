package com.example.userservice.controller;

import com.example.userservice.dto.DailySummaryDTO;
import com.example.userservice.dto.TransactionTimeDTO;
import com.example.userservice.entity.PerformanceMetrics;
import com.example.userservice.service.TechnicalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technical")
public class TechnicalController {
    private final TechnicalService technicalService;

    public TechnicalController(TechnicalService technicalService) {
        this.technicalService = technicalService;
    }

    @GetMapping("/failures")
    public List<PerformanceMetrics> getFailures(@RequestParam(required = false) String date,
                                                @RequestParam(required = false) Integer locationId) {
        return technicalService.getFailuresByDateAndLocation(date, locationId);
    }

    @GetMapping("/execution-times")
    public List<TransactionTimeDTO> getExecutionTimes(@RequestParam(required = false) String date,
                                                      @RequestParam(required = false) Integer locationId) {
        return technicalService.getExecutionTimes(date, locationId);
    }

    @GetMapping("/daily-summary")
    public List<DailySummaryDTO> getDailySummary() {
        return technicalService.getDailySummaries();
    }
}
