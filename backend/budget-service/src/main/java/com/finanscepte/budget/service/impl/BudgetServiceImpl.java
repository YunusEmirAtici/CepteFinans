package com.finanscepte.budget.service.impl;

import com.finanscepte.common.exception.ResourceNotFoundException;
import com.finanscepte.budget.model.Budget;
import com.finanscepte.budget.repository.BudgetRepository;
import com.finanscepte.budget.service.BudgetService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;

    @Override
    public Budget create(Budget entity) {
        return budgetRepository.save(entity);
    }

    @Override
    public Budget getById(String id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found: " + id));
    }

    @Override
    public List<Budget> getAll() {
        return budgetRepository.findAll();
    }

    @Override
    public void delete(String id) {
        budgetRepository.deleteById(id);
    }

    @Override
    public List<Budget> findByUserId(String userId) {
        return budgetRepository.findByUserId(userId);
    }

    @Override
    public List<Budget> findByUserIdAndMonthAndYear(String userId, int month, int year) {
        return budgetRepository.findByUserIdAndMonthAndYear(userId, month, year);
    }
}
