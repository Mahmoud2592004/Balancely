package com.example.userservice.exception;

public class InsufficientBalanceException extends RuntimeException {
    private final String errorCode;

    public InsufficientBalanceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}