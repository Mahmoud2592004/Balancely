package com.example.userservice.dto;

import java.time.Duration;
import java.time.LocalDateTime;

public class TransactionTimeDTO {
    private Long transactionId;
    private LocalDateTime start;
    private LocalDateTime end;

    public TransactionTimeDTO(Long transactionId, LocalDateTime start, LocalDateTime end) {
        this.transactionId = transactionId;
        this.start = start;
        this.end = end;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Duration getDuration() {
        return Duration.between(start, end);
    }
}
