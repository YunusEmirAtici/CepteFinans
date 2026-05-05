package com.finanscepte.budget.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "budgets")
public class Budget {
    @Id
    private String id;
    private String userId;
    private String category;
    private BigDecimal limitAmount;
    private BigDecimal spentAmount;
    private int month;
    private int year;
    private LocalDateTime createdAt;
}
