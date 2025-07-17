package com.example.userservice.service;

import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.entity.Balance;
import com.example.userservice.entity.Location;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.repository.BalanceRepository;
import com.example.userservice.repository.LocationRepository;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final RoleRepository roleRepository;
    private final LocationRepository locationRepository;

    public User createUser(CreateUserRequest request) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Invalid role"));
        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Invalid location"));

        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setPasswordHash(request.getPassword());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(role);
        user.setLocation(location);

        User savedUser = userRepository.save(user);

        Balance balance = new Balance();
        balance.setUser(savedUser);
        balance.setEntityType(role.getName()); // "POS" or "Agent"
        balanceRepository.save(balance);

        return savedUser;
    }
}
