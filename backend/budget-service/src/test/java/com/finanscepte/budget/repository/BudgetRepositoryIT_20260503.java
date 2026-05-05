package com.finanscepte.budget.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.finanscepte.budget.model.Budget;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataMongoTest
@Testcontainers
class BudgetRepositoryIT_20260503 {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private BudgetRepository budgetRepository;

    @Test
    void shouldPersistAndReadBudget() {
        Budget saved = budgetRepository.save(Budget.builder()
                .userId("u1")
                .category("Food")
                .limitAmount(BigDecimal.valueOf(1000))
                .spentAmount(BigDecimal.valueOf(500))
                .month(5)
                .year(2026)
                .createdAt(LocalDateTime.now())
                .build());

        Budget found = budgetRepository.findById(saved.getId()).orElseThrow();
        assertEquals("Food", found.getCategory());
        assertEquals(0, found.getLimitAmount().compareTo(BigDecimal.valueOf(1000)));
    }

    @Test
    void shouldFindByUserId() {
        budgetRepository.save(Budget.builder().userId("u1").category("Food").limitAmount(BigDecimal.valueOf(500)).spentAmount(BigDecimal.ZERO).month(5).year(2026).createdAt(LocalDateTime.now()).build());
        budgetRepository.save(Budget.builder().userId("u1").category("Transport").limitAmount(BigDecimal.valueOf(300)).spentAmount(BigDecimal.ZERO).month(5).year(2026).createdAt(LocalDateTime.now()).build());
        budgetRepository.save(Budget.builder().userId("u2").category("Food").limitAmount(BigDecimal.valueOf(400)).spentAmount(BigDecimal.ZERO).month(5).year(2026).createdAt(LocalDateTime.now()).build());

        List<Budget> found = budgetRepository.findByUserId("u1");
        assertEquals(2, found.size());
    }

    @Test
    void shouldFindByUserIdAndMonthAndYear() {
        budgetRepository.save(Budget.builder().userId("u1").category("Food").limitAmount(BigDecimal.valueOf(500)).spentAmount(BigDecimal.ZERO).month(5).year(2026).createdAt(LocalDateTime.now()).build());
        budgetRepository.save(Budget.builder().userId("u1").category("Food").limitAmount(BigDecimal.valueOf(600)).spentAmount(BigDecimal.ZERO).month(6).year(2026).createdAt(LocalDateTime.now()).build());

        List<Budget> found = budgetRepository.findByUserIdAndMonthAndYear("u1", 5, 2026);
        assertEquals(1, found.size());
        assertEquals(0, found.get(0).getLimitAmount().compareTo(BigDecimal.valueOf(500)));
    }

    @Test
    void shouldDeleteBudget() {
        Budget saved = budgetRepository.save(Budget.builder()
                .userId("u1")
                .category("DeleteMe")
                .limitAmount(BigDecimal.valueOf(100))
                .spentAmount(BigDecimal.ZERO)
                .month(1)
                .year(2026)
                .createdAt(LocalDateTime.now())
                .build());

        budgetRepository.deleteById(saved.getId());
        assertTrue(budgetRepository.findById(saved.getId()).isEmpty());
    }
}
