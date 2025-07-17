package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    private LocalDateTime timestamp;

    @PrePersist
    public void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

    @Column(nullable = false)
    private String status;
}
