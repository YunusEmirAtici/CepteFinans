package com.finanscepte.desktop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Budget(String id, String userId, String category, BigDecimal limitAmount, BigDecimal spentAmount, int month, int year, LocalDateTime createdAt, boolean exceeded) {}
