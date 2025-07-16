package com.example.userservice.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requester_id")
    private Long requesterId;

    @Column(name = "target_id")
    private Long targetId;

    private String channel;

    @Column(name = "transaction_type")
    private String transactionType;

    private BigDecimal amount;

    private String status;

    @Column(name = "recharge_card_id")
    private Long rechargeCardId;

    private LocalDateTime timestamp;

    @Column(name = "is_fraud")
    private Boolean isFraud;

    @Column(name = "fraud_score")
    private Double fraudScore;

    // === Getters ===

    public Long getId() {
        return id;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public String getChannel() {
        return channel;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public Long getRechargeCardId() {
        return rechargeCardId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Boolean getIsFraud() {
        return isFraud;
    }

    public Double getFraudScore() {
        return fraudScore;
    }

    // === Setters ===

    public void setId(Long id) {
        this.id = id;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRechargeCardId(Long rechargeCardId) {
        this.rechargeCardId = rechargeCardId;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setIsFraud(Boolean isFraud) {
        this.isFraud = isFraud;
    }

    public void setFraudScore(Double fraudScore) {
        this.fraudScore = fraudScore;
    }
}
