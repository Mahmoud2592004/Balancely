package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PerformanceMetricsDTO {
    private Object period;
    private Long totalTransactions;
    private Long failedTransactions;
    private Double averageExecutionTime;
}