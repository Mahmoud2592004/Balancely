package com.example.userservice.controller;

import com.example.userservice.dto.BuyCardRequest;
import com.example.userservice.dto.CardStatisticsDTO;
import com.example.userservice.entity.RechargeCard;
import com.example.userservice.service.RechargeCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pos/cards")
@RequiredArgsConstructor
public class RechargeCardController {

    private final RechargeCardService rechargeCardService;

    @PostMapping("/buy")
    public List<RechargeCard> buyCards(
            Authentication authentication,
            @RequestBody BuyCardRequest request) {
        String posUsername = authentication.getName();
        return rechargeCardService.buyRechargeCards(
                posUsername,
                request.getCardValue(),
                request.getQuantity()
        );
    }

    @GetMapping("/my-cards")
    public List<RechargeCard> getMyCards(Authentication authentication) {
        String posUsername = authentication.getName();
        return rechargeCardService.getMyCards(posUsername);
    }

    @GetMapping("/my-statistics")
    public List<CardStatisticsDTO> getMyCardStatistics(Authentication authentication) {
        String posUsername = authentication.getName();
        return rechargeCardService.getMyCardStatistics(posUsername);
    }

    @GetMapping("/my-statistics/{value}")
    public CardStatisticsDTO getMyCardStatisticsByValue(
            Authentication authentication,
            @PathVariable BigDecimal value) {
        String posUsername = authentication.getName();
        return rechargeCardService.getMyCardStatisticsByValue(posUsername, value);
    }

    @GetMapping("/my-statistics/overall")
    public CardStatisticsDTO getMyOverallCardStatistics(Authentication authentication) {
        String posUsername = authentication.getName();
        return rechargeCardService.getMyOverallCardStatistics(posUsername);
    }
}