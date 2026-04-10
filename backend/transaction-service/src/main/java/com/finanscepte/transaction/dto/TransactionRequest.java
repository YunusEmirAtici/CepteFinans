package com.finanscepte.transaction.dto;

import com.finanscepte.transaction.model.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        @NotBlank(message = "userId bos olamaz") String userId,
        @NotNull(message = "amount bos olamaz")
        @DecimalMin(value = "0.01", message = "amount sifirdan buyuk olmali") BigDecimal amount,
        @NotNull(message = "type bos olamaz") TransactionType type,
        @NotBlank(message = "category bos olamaz") String category,
        @NotNull(message = "transactionDate bos olamaz") LocalDate transactionDate,
        String description,
        boolean recurring
) {
}
