package com.example.userservice.controller;

import com.example.userservice.dto.BalanceRechargeRequest;
import com.example.userservice.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @PostMapping("/recharge-pos")
    public String rechargePOS(
            Authentication authentication,
            @RequestBody BalanceRechargeRequest request) {
        String agentUsername = authentication.getName();
        return agentService.rechargePOSBalance(agentUsername, request);
    }
}