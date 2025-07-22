package com.example.userservice.service;

import java.math.BigDecimal;

public class CardShardSelector {
    public static String getShard(BigDecimal value) {
        // Use the card value itself as the shard key, ensuring uniqueness
        return "SHARD_" + value.toString().replace(".", "_");
    }
}