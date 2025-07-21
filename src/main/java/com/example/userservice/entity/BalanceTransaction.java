package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "balance_transactions")
@Getter
@Setter
public class BalanceTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String transactionType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "source_id", nullable = false)
    private User source;

    @ManyToOne(optional = false)
    @JoinColumn(name = "destination_id", nullable = false)
    private User destination;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "execution_time")
    private Long executionTime; // in milliseconds

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "device_hash")
    private String deviceHash;

    @Column(name = "location")
    private String location;

    @Column(name = "fraud_score")
    private Double fraudScore;

    @Column(name = "is_fraud")
    private Boolean isFraud = false;

    @Column(nullable = false)
    private String status;

    @PostPersist
    public void calculateDuration() {
        if (startTime != null && endTime != null) {
            this.executionTime = Duration.between(startTime, endTime).toMillis();
        }
    }
}