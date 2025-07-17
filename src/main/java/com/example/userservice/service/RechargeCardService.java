package com.example.userservice.service;

import com.example.userservice.entity.RechargeCard;
import com.example.userservice.entity.User;
import com.example.userservice.repository.RechargeCardRepository;
import com.example.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RechargeCardService {

    private final RechargeCardRepository rechargeCardRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<RechargeCard> buyRechargeCards(String username, String password, BigDecimal value, int quantity) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Simple password check (in production, use hashing)
        if (!user.getPasswordHash().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        // Fetch available cards with specified value
        List<RechargeCard> availableCards = rechargeCardRepository
                .findByIsUsedFalseAndValue(value);

        if (availableCards.size() < quantity) {
            throw new RuntimeException("Not enough cards of value " + value + " available");
        }

        List<RechargeCard> cardsToAssign = availableCards.subList(0, quantity);

        for (RechargeCard card : cardsToAssign) {
            card.setUsed(true);
            card.setUsedBy(user);
            card.setUsedAt(LocalDateTime.now());
        }

        return rechargeCardRepository.saveAll(cardsToAssign);
    }


    public List<RechargeCard> getMyCards(Long userId) {
        return rechargeCardRepository.findByUsedById(userId);
    }
}
