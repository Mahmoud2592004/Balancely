package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recharge_cards")
@Getter
@Setter
public class RechargeCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(name = "is_used")
    private boolean isUsed = false;

    @ManyToOne
    @JoinColumn(name = "used_by")
    private User usedBy;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Version
    private Long version;
}