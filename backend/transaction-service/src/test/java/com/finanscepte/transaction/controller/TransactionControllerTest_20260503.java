package com.finanscepte.transaction.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.finanscepte.transaction.dto.TransactionRequest;
import com.finanscepte.transaction.dto.TransactionResponse;
import com.finanscepte.transaction.model.Transaction;
import com.finanscepte.transaction.model.TransactionType;
import com.finanscepte.transaction.service.TransactionService;
import com.finanscepte.transaction.util.TransactionMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest_20260503 {

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void shouldReturnTransactionList() {
        Transaction tx = Transaction.builder().id("1").userId("u1").amount(BigDecimal.TEN).type(TransactionType.EXPENSE).category("Food").transactionDate(LocalDate.now()).build();
        TransactionResponse response = new TransactionResponse("1", "u1", BigDecimal.TEN, TransactionType.EXPENSE, "Food", LocalDate.now(), null, false);
        when(transactionService.getAll()).thenReturn(List.of(tx));
        when(transactionMapper.toResponse(tx)).thenReturn(response);

        List<TransactionResponse> result = transactionController.getAll(null, null);

        assertEquals(1, result.size());
        assertEquals("Food", result.get(0).category());
    }

    @Test
    void shouldCreateTransaction() {
        TransactionRequest request = new TransactionRequest("u1", BigDecimal.valueOf(100), TransactionType.INCOME, "Salary", LocalDate.now(), "Monthly salary", false);
        Transaction entity = Transaction.builder().userId("u1").amount(BigDecimal.valueOf(100)).type(TransactionType.INCOME).category("Salary").transactionDate(LocalDate.now()).description("Monthly salary").recurring(false).build();
        Transaction saved = Transaction.builder().id("2").userId("u1").amount(BigDecimal.valueOf(100)).type(TransactionType.INCOME).category("Salary").transactionDate(LocalDate.now()).description("Monthly salary").recurring(false).build();
        TransactionResponse response = new TransactionResponse("2", "u1", BigDecimal.valueOf(100), TransactionType.INCOME, "Salary", LocalDate.now(), "Monthly salary", false);

        when(transactionMapper.toEntity(request)).thenReturn(entity);
        when(transactionService.create(entity)).thenReturn(saved);
        when(transactionMapper.toResponse(saved)).thenReturn(response);

        TransactionResponse result = transactionController.create(request);

        assertNotNull(result);
        assertEquals("2", result.id());
    }

    @Test
    void shouldFilterByUserIdAndType() {
        Transaction tx = Transaction.builder().id("1").userId("u1").amount(BigDecimal.TEN).type(TransactionType.EXPENSE).category("Food").transactionDate(LocalDate.now()).build();
        TransactionResponse response = new TransactionResponse("1", "u1", BigDecimal.TEN, TransactionType.EXPENSE, "Food", LocalDate.now(), null, false);
        when(transactionService.getByUserIdAndType("u1", TransactionType.EXPENSE)).thenReturn(List.of(tx));
        when(transactionMapper.toResponse(tx)).thenReturn(response);

        List<TransactionResponse> result = transactionController.getAll("u1", TransactionType.EXPENSE);

        assertEquals(1, result.size());
    }

    @Test
    void shouldDeleteTransaction() {
        transactionController.delete("9");
        verify(transactionService).delete("9");
    }
}
