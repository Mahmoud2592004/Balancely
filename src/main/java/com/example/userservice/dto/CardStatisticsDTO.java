package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class CardStatisticsDTO {
    private BigDecimal cardValue;
    private long totalCards;
    private long usedCards;
    private long unusedCards;

    // Constructor for value-specific statistics
    public CardStatisticsDTO(BigDecimal cardValue, long totalCards, long usedCards) {
        this.cardValue = cardValue;
        this.totalCards = totalCards;
        this.usedCards = usedCards;
        this.unusedCards = totalCards - usedCards;
    }

    // Constructor for overall statistics
    public CardStatisticsDTO(long totalCards, long usedCards) {
        this.cardValue = BigDecimal.ZERO; // Use zero for overall stats
        this.totalCards = totalCards;
        this.usedCards = usedCards;
        this.unusedCards = totalCards - usedCards;
    }
}