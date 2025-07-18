package com.example.userservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceRechargeRequest {
    private String agentUsername;
    private String agentPassword;
    private String posUsername;
    private BigDecimal amount;
//    private String ipAddress;
//    private String deviceHash;
//    private String location;
}
