package com.finanscepte.budget.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("service", "budget-service", "status", "ok");
    }
}
