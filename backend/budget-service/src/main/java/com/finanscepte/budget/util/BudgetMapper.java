package com.finanscepte.budget.util;

import com.finanscepte.budget.dto.BudgetRequest;
import com.finanscepte.budget.dto.BudgetResponse;
import com.finanscepte.budget.model.Budget;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper {

    public Budget toEntity(BudgetRequest request) {
        return Budget.builder()
                .userId(request.userId())
                .category(request.category())
                .limitAmount(request.limitAmount())
                .spentAmount(request.spentAmount() != null ? request.spentAmount() : BigDecimal.ZERO)
                .month(request.month())
                .year(request.year())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public BudgetResponse toResponse(Budget budget) {
        boolean exceeded = budget.getSpentAmount() != null
                && budget.getLimitAmount() != null
                && budget.getSpentAmount().compareTo(budget.getLimitAmount()) > 0;
        return new BudgetResponse(
                budget.getId(),
                budget.getUserId(),
                budget.getCategory(),
                budget.getLimitAmount(),
                budget.getSpentAmount(),
                budget.getMonth(),
                budget.getYear(),
                budget.getCreatedAt(),
                exceeded
        );
    }
}
