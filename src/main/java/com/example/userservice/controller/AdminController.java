package com.example.userservice.controller;

import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.RechargeAgentRequest;
import com.example.userservice.entity.User;
import com.example.userservice.service.AdminService;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/recharge-agent")
    public ResponseEntity<String> rechargeAgent(@RequestBody RechargeAgentRequest request) {
        String message = adminService.rechargeAgentBalance(request);
        return ResponseEntity.ok(message);
    }
}
