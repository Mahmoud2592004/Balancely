package com.example.userservice.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recharge_cards")
public class RechargeCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private BigDecimal value;

    @Column(name = "is_used")
    private Boolean isUsed;

    @Column(name = "used_by")
    private Long usedBy;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    // === Getters and Setters ===

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public Long getUsedBy() {
        return usedBy;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public void setUsedBy(Long usedBy) {
        this.usedBy = usedBy;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }
}
