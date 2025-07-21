package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GenerateCardsRequest {
    private String adminUsername;
    private String adminPassword;
    private BigDecimal cardValue;
    private int quantity;
}