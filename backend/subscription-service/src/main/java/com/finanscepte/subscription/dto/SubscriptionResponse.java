package com.finanscepte.subscription.dto;

import com.finanscepte.subscription.model.BillingCycle;
import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionResponse(
        String id,
        String userId,
        String name,
        BigDecimal amount,
        BillingCycle billingCycle,
        LocalDate startDate,
        LocalDate nextPaymentDate,
        String category,
        boolean reminderEnabled,
        String notes,
        boolean active
) {
}
