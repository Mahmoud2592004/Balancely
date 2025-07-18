package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class SalesStatisticsDTO {
    private Object period;
    private Long totalTransactions;
    private BigDecimal totalSales;
    private BigDecimal totalProfit;
}