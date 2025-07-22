package com.example.userservice.controller;

import com.example.userservice.dto.BuyCardRequest;
import com.example.userservice.dto.CardStatisticsDTO;
import com.example.userservice.entity.RechargeCard;
import com.example.userservice.service.RechargeCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pos/cards")
@RequiredArgsConstructor
public class RechargeCardController {

    private final RechargeCardService rechargeCardService;

    @PostMapping("/buy")
    public ResponseEntity<List<RechargeCard>> buyCards(
            Authentication authentication,
            @Valid @RequestBody BuyCardRequest request) {
        String posUsername = authentication.getName();
        List<RechargeCard> cards = rechargeCardService.buyRechargeCards(posUsername, request);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/my-cards")
    public ResponseEntity<List<RechargeCard>> getMyCards(Authentication authentication) {
        String posUsername = authentication.getName();
        List<RechargeCard> cards = rechargeCardService.getMyCards(posUsername);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/my-statistics")
    public ResponseEntity<List<CardStatisticsDTO>> getMyCardStatistics(Authentication authentication) {
        String posUsername = authentication.getName();
        List<CardStatisticsDTO> statistics = rechargeCardService.getMyCardStatistics(posUsername);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/my-statistics/{value}")
    public ResponseEntity<CardStatisticsDTO> getMyCardStatisticsByValue(
            Authentication authentication,
            @PathVariable BigDecimal value) {
        String posUsername = authentication.getName();
        CardStatisticsDTO statistics = rechargeCardService.getMyCardStatisticsByValue(posUsername, value);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/my-statistics/overall")
    public ResponseEntity<CardStatisticsDTO> getMyOverallCardStatistics(Authentication authentication) {
        String posUsername = authentication.getName();
        CardStatisticsDTO statistics = rechargeCardService.getMyOverallCardStatistics(posUsername);
        return ResponseEntity.ok(statistics);
    }
}