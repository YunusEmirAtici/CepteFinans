package com.finanscepte.budget.repository;

import com.finanscepte.budget.model.Budget;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BudgetRepository extends MongoRepository<Budget, String> {
    List<Budget> findByUserId(String userId);
    List<Budget> findByUserIdAndMonthAndYear(String userId, int month, int year);
}
