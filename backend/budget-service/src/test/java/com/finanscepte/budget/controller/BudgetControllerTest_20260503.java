package com.finanscepte.budget.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.finanscepte.budget.dto.BudgetResponse;
import com.finanscepte.budget.model.Budget;
import com.finanscepte.budget.service.BudgetService;
import com.finanscepte.budget.util.BudgetMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BudgetControllerTest_20260503 {

    @Mock
    private BudgetService budgetService;

    @Mock
    private BudgetMapper budgetMapper;

    @InjectMocks
    private BudgetController budgetController;

    @Test
    void shouldReturnBudgetList() {
        Budget budget = Budget.builder().id("1").userId("u1").category("Food").limitAmount(BigDecimal.TEN).month(5).year(2026).build();
        BudgetResponse response = new BudgetResponse("1", "u1", "Food", BigDecimal.TEN, BigDecimal.ZERO, 5, 2026, null, false);
        when(budgetService.getAll()).thenReturn(List.of(budget));
        when(budgetMapper.toResponse(budget)).thenReturn(response);

        List<BudgetResponse> budgets = budgetController.getAll();

        assertEquals(1, budgets.size());
    }
}
