package com.finanscepte.subscription.dto;

import com.finanscepte.subscription.model.BillingCycle;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionRequest(
        @NotBlank(message = "userId bos olamaz") String userId,
        @NotBlank(message = "name bos olamaz") String name,
        @NotNull(message = "amount bos olamaz")
        @DecimalMin(value = "0.01", message = "amount sifirdan buyuk olmali") BigDecimal amount,
        @NotNull(message = "billingCycle bos olamaz") BillingCycle billingCycle,
        @NotNull(message = "startDate bos olamaz") LocalDate startDate,
        @NotNull(message = "nextPaymentDate bos olamaz") LocalDate nextPaymentDate,
        @NotBlank(message = "category bos olamaz") String category,
        boolean reminderEnabled,
        String notes,
        boolean active
) {
}
