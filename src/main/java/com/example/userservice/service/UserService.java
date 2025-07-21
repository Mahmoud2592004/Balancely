package com.example.userservice.service;

import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.entity.*;
import com.example.userservice.exception.*;
import com.example.userservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final RoleRepository roleRepository;
    private final LocationRepository locationRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(CreateUserRequest request) {
        validateCreateUserRequest(request);

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("INVALID_ROLE", "Role with ID '" + request.getRoleId() + "' not found"));
        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("INVALID_LOCATION", "Location with ID '" + request.getLocationId() + "' not found"));

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ValidationException("USERNAME_EXISTS", "Username '" + request.getUsername() + "' is already taken");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(role);
        user.setLocation(location);

        // Create and associate balance
        Balance balance = new Balance();
        balance.setUser(user);
        balance.setEntityType(role.getName());
        balance.setCurrentBalance(BigDecimal.ZERO);

        // Save user first to generate ID
        User savedUser = userRepository.save(user);

        // Then save balance
        balanceRepository.save(balance);

        // Update user with balance reference
        savedUser.setBalance(balance);
        userRepository.save(savedUser);

        return savedUser;
    }

    private void validateCreateUserRequest(CreateUserRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new ValidationException("INVALID_USERNAME", "Username cannot be empty");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new ValidationException("INVALID_PASSWORD", "Password must be at least 6 characters long");
        }
        if (request.getFullName() == null || request.getFullName().isBlank()) {
            throw new ValidationException("INVALID_FULL_NAME", "Full name cannot be empty");
        }
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank()) {
            throw new ValidationException("INVALID_PHONE_NUMBER", "Phone number cannot be empty");
        }
        if (request.getRoleId() == null) {
            throw new ValidationException("INVALID_ROLE_ID", "Role ID cannot be null");
        }
        if (request.getLocationId() == null) {
            throw new ValidationException("INVALID_LOCATION_ID", "Location ID cannot be null");
        }
    }
}