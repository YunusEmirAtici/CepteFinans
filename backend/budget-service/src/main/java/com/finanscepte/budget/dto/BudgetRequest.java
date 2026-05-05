package com.finanscepte.budget.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record BudgetRequest(
        @NotBlank(message = "userId bos olamaz") String userId,
        @NotBlank(message = "category bos olamaz") String category,
        @DecimalMin(value = "0.0", inclusive = false, message = "limitAmount sifirdan buyuk olmali") BigDecimal limitAmount,
        BigDecimal spentAmount,
        @Min(value = 1, message = "month 1-12 arasi olmali") int month,
        @Min(value = 2000, message = "year gecerli olmali") int year
) {
}
