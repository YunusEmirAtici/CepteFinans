package com.finanscepte.subscription.util;

import com.finanscepte.subscription.dto.SubscriptionRequest;
import com.finanscepte.subscription.dto.SubscriptionResponse;
import com.finanscepte.subscription.model.Subscription;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {

    public Subscription toEntity(SubscriptionRequest request) {
        return Subscription.builder()
                .userId(request.userId())
                .name(request.name())
                .amount(request.amount())
                .billingCycle(request.billingCycle())
                .startDate(request.startDate())
                .nextPaymentDate(request.nextPaymentDate())
                .category(request.category())
                .reminderEnabled(request.reminderEnabled())
                .notes(request.notes())
                .active(request.active())
                .build();
    }

    public SubscriptionResponse toResponse(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.getId(),
                subscription.getUserId(),
                subscription.getName(),
                subscription.getAmount(),
                subscription.getBillingCycle(),
                subscription.getStartDate(),
                subscription.getNextPaymentDate(),
                subscription.getCategory(),
                subscription.isReminderEnabled(),
                subscription.getNotes(),
                subscription.isActive()
        );
    }
}
