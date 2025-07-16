package com.example.userservice.dto;

public class DailySummaryDTO {
    private String date;
    private int totalTransactions;
    private int failedTransactions;
    private double averageExecutionTimeSeconds;

    public DailySummaryDTO() {}

    public DailySummaryDTO(String date, int totalTransactions, int failedTransactions, double averageExecutionTimeSeconds) {
        this.date = date;
        this.totalTransactions = totalTransactions;
        this.failedTransactions = failedTransactions;
        this.averageExecutionTimeSeconds = averageExecutionTimeSeconds;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public int getFailedTransactions() {
        return failedTransactions;
    }

    public void setFailedTransactions(int failedTransactions) {
        this.failedTransactions = failedTransactions;
    }

    public double getAverageExecutionTimeSeconds() {
        return averageExecutionTimeSeconds;
    }

    public void setAverageExecutionTimeSeconds(double averageExecutionTimeSeconds) {
        this.averageExecutionTimeSeconds = averageExecutionTimeSeconds;
    }
}