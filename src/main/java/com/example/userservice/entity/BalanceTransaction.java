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

    @ManyToOne
    @JoinColumn(name = "source_entity_id")
    private User source;

    @ManyToOne
    @JoinColumn(name = "destination_entity_id")
    private User destination;

    @Column(nullable = false)
    private BigDecimal amount;

    private String transactionType = "recharge";

    private String status = "success";

    private String reason;

    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}
