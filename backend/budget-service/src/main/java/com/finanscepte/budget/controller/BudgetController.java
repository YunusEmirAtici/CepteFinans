package com.finanscepte.budget.controller;

import com.finanscepte.budget.dto.BudgetRequest;
import com.finanscepte.budget.dto.BudgetResponse;
import com.finanscepte.budget.service.BudgetService;
import com.finanscepte.budget.util.BudgetMapper;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final BudgetMapper budgetMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BudgetResponse create(@Valid @RequestBody BudgetRequest request) {
        return budgetMapper.toResponse(budgetService.create(budgetMapper.toEntity(request)));
    }

    @GetMapping("/{id}")
    public BudgetResponse getById(@PathVariable String id) {
        return budgetMapper.toResponse(budgetService.getById(id));
    }

    @GetMapping
    public List<BudgetResponse> getAll() {
        return budgetService.getAll().stream().map(budgetMapper::toResponse).toList();
    }

    @GetMapping("/user/{userId}")
    public List<BudgetResponse> getByUserId(@PathVariable String userId) {
        return budgetService.findByUserId(userId).stream().map(budgetMapper::toResponse).toList();
    }

    @GetMapping("/user/{userId}/status")
    public List<BudgetResponse> getByUserIdAndMonthAndYear(
            @PathVariable String userId,
            @RequestParam int month,
            @RequestParam int year) {
        return budgetService.findByUserIdAndMonthAndYear(userId, month, year)
                .stream().map(budgetMapper::toResponse).toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        budgetService.delete(id);
    }

    @GetMapping("/health")
    public java.util.Map<String, String> health() {
        return java.util.Map.of("service", "budget-service", "status", "ok");
    }
}
