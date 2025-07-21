package com.example.userservice.exception;

// Update existing UserNotFoundException
public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String message) {
        super("USER_NOT_FOUND", message);
    }
}