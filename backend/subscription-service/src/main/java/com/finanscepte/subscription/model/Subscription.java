package com.finanscepte.subscription.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "subscriptions")
public class Subscription {
    @Id
    private String id;
    private String userId;
    private String name;
    private BigDecimal amount;
    private BillingCycle billingCycle;
    private LocalDate startDate;
    private LocalDate nextPaymentDate;
    private String category;
    private boolean reminderEnabled;
    private String notes;
    private boolean active;
}
