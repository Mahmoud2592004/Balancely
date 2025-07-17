package com.example.userservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BuyCardRequest {
    private String username;
    private String password; // plain password for now
    private BigDecimal cardValue;
    private int quantity;
}
