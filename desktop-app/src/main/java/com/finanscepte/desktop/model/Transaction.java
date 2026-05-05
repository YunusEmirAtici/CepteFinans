package com.finanscepte.desktop.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction(
        String id,
        String userId,
        BigDecimal amount,
        String type,
        String category,
        LocalDate transactionDate,
        String description,
        boolean recurring
) {}
