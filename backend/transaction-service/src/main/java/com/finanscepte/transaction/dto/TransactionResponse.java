package com.finanscepte.transaction.dto;

import com.finanscepte.transaction.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        String id,
        String userId,
        BigDecimal amount,
        TransactionType type,
        String category,
        LocalDate transactionDate,
        String description,
        boolean recurring
) {
}
