package com.example.userservice.controller;

import com.example.userservice.dto.CardStatisticsDTO;
import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.GenerateCardsRequest;
import com.example.userservice.dto.RechargeAgentRequest;
import com.example.userservice.entity.RechargeCard;
import com.example.userservice.entity.User;
import com.example.userservice.service.AdminService;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
    public ResponseEntity<String> rechargeAgent(
            Authentication authentication,
            @RequestBody RechargeAgentRequest request) {
        String adminUsername = authentication.getName();
        String message = adminService.rechargeAgentBalance(adminUsername, request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/generate-cards")
    public ResponseEntity<List<RechargeCard>> generateCards(@RequestBody GenerateCardsRequest request) {
        List<RechargeCard> cards = adminService.generateRechargeCards(request);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/card-statistics")
    public ResponseEntity<List<CardStatisticsDTO>> getCardStatistics() {
        return ResponseEntity.ok(adminService.getCardStatistics());
    }

    @GetMapping("/card-statistics/{value}")
    public ResponseEntity<CardStatisticsDTO> getCardStatisticsByValue(
            @PathVariable BigDecimal value) {
        return ResponseEntity.ok(adminService.getCardStatisticsByValue(value));
    }

    @GetMapping("/card-statistics/overall")
    public ResponseEntity<CardStatisticsDTO> getOverallCardStatistics() {
        return ResponseEntity.ok(adminService.getOverallCardStatistics());
    }
}