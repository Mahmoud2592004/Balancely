// PerformanceMetrics.java
package com.example.userservice.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "performance_metrics")
public class PerformanceMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "failed_transaction_number")
    private int failedTransactionNumber;

    @Column(name = "location_id")
    private Integer locationId;

    private LocalDate date;

    public Long getId() { return id; }
    public int getFailedTransactionNumber() { return failedTransactionNumber; }
    public Integer getLocationId() { return locationId; }
    public LocalDate getDate() { return date; }

    public void setId(Long id) { this.id = id; }
    public void setFailedTransactionNumber(int failedTransactionNumber) { this.failedTransactionNumber = failedTransactionNumber; }
    public void setLocationId(Integer locationId) { this.locationId = locationId; }
    public void setDate(LocalDate date) { this.date = date; }
}