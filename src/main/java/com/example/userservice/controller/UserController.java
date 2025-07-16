package com.example.userservice.controller;

import com.example.userservice.entity.Transaction;
import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> allUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    public User getByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{userId}/balance")
    public BigDecimal getBalance(@PathVariable Long userId) {
        return userService.getUserById(userId).getBalance();
    }

    @GetMapping("/{userId}/transactions")
    public List<Transaction> getHistory(@PathVariable Long userId) {
        return userService.getTransactionHistory(userId);
    }

    @PostMapping("/{userId}/recharge/card")
    public Transaction rechargeWithCard(
            @PathVariable Long userId,
            @RequestParam String posUsername,
            @RequestParam String posPassword,
            @RequestParam String code
    ) {
        return userService.rechargeUsingCard(userId, posUsername, posPassword, code);
    }


}
