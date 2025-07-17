package com.example.userservice.controller;

import com.example.userservice.dto.BuyCardRequest;
import com.example.userservice.entity.RechargeCard;
import com.example.userservice.service.RechargeCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pos/cards")
@RequiredArgsConstructor
public class RechargeCardController {

    private final RechargeCardService rechargeCardService;

    @PostMapping("/buy")
    public List<RechargeCard> buyCards(@RequestBody BuyCardRequest request) {
        return rechargeCardService.buyRechargeCards(
                request.getUsername(),
                request.getPassword(),
                request.getCardValue(),
                request.getQuantity()
        );
    }


    @GetMapping("/my-cards")
    public List<RechargeCard> getMyCards(@RequestParam Long userId) {
        return rechargeCardService.getMyCards(userId);
    }
}
