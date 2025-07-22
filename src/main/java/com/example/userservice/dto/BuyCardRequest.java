package com.example.userservice.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class BuyCardRequest {
    @NotNull(message = "Card value cannot be null")
    @Positive(message = "Card value must be greater than zero")
    private BigDecimal cardValue;

    @Positive(message = "Quantity must be greater than zero")
    private int quantity;
}