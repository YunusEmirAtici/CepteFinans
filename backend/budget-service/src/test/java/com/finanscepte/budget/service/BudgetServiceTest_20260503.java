package com.finanscepte.budget.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.finanscepte.common.exception.ResourceNotFoundException;
import com.finanscepte.budget.model.Budget;
import com.finanscepte.budget.repository.BudgetRepository;
import com.finanscepte.budget.service.impl.BudgetServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest_20260503 {

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    @Test
    void shouldCreateBudget() {
        Budget request = Budget.builder().userId("u1").category("Food").limitAmount(BigDecimal.valueOf(1000)).month(5).year(2026).build();
        Budget saved = Budget.builder().id("1").userId("u1").category("Food").limitAmount(BigDecimal.valueOf(1000)).month(5).year(2026).build();
        when(budgetRepository.save(request)).thenReturn(saved);

        Budget result = budgetService.create(request);

        assertEquals("1", result.getId());
    }

    @Test
    void shouldThrowNotFoundWhenBudgetMissing() {
        when(budgetRepository.findById("404")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> budgetService.getById("404"));
    }

    @Test
    void shouldDeleteBudgetById() {
        budgetService.delete("9");
        verify(budgetRepository).deleteById("9");
    }

    @Test
    void shouldFindByUserId() {
        Budget budget = Budget.builder().id("1").userId("u1").category("Food").limitAmount(BigDecimal.valueOf(500)).month(5).year(2026).build();
        when(budgetRepository.findByUserId("u1")).thenReturn(List.of(budget));

        List<Budget> result = budgetService.findByUserId("u1");

        assertEquals(1, result.size());
    }

    @Test
    void shouldFindByUserIdAndMonthAndYear() {
        Budget budget = Budget.builder().id("1").userId("u1").category("Food").limitAmount(BigDecimal.valueOf(500)).month(5).year(2026).build();
        when(budgetRepository.findByUserIdAndMonthAndYear("u1", 5, 2026)).thenReturn(List.of(budget));

        List<Budget> result = budgetService.findByUserIdAndMonthAndYear("u1", 5, 2026);

        assertEquals(1, result.size());
    }
}
