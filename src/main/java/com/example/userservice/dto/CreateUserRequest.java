package com.example.userservice.dto;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String fullName;
    private String username;
    private String password;
    private String phoneNumber;
    private Long roleId;
    private Long locationId;
}
