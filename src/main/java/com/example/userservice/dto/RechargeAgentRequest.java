package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RechargeAgentRequest {
    private String adminUsername;
    private String adminPassword;
    private String agentUsername;
    private BigDecimal amount;
}
