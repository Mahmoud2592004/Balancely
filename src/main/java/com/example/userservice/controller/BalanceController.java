package com.example.userservice.controller;

import com.example.userservice.entity.Balance;
import com.example.userservice.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BalanceController {
    private final BalanceService balanceService;

    @GetMapping("/balance")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == #username")
    public Balance getBalance(Authentication authentication, @RequestParam String username) {
        return balanceService.getBalance(username);
    }
}