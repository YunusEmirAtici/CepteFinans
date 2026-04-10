package com.finanscepte.transaction.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.finanscepte.transaction.exception.ResourceNotFoundException;
import com.finanscepte.transaction.model.Transaction;
import com.finanscepte.transaction.model.TransactionType;
import com.finanscepte.transaction.repository.TransactionRepository;
import com.finanscepte.transaction.service.impl.TransactionServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest_20260410 {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void shouldReturnTransactionById() {
        Transaction t = Transaction.builder()
                .id("1")
                .userId("u1")
                .amount(BigDecimal.TEN)
                .type(TransactionType.EXPENSE)
                .category("food")
                .transactionDate(LocalDate.now())
                .build();
        when(transactionRepository.findById("1")).thenReturn(Optional.of(t));
        Transaction result = transactionService.getById("1");
        assertEquals("u1", result.getUserId());
    }

    @Test
    void shouldThrowWhenTransactionMissing() {
        when(transactionRepository.findById("99")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> transactionService.getById("99"));
    }

    @Test
    void shouldFilterByUserAndType() {
        when(transactionRepository.findByUserIdAndType("u1", TransactionType.INCOME))
                .thenReturn(List.of(Transaction.builder().id("5").userId("u1").build()));
        List<Transaction> result = transactionService.getByUserIdAndType("u1", TransactionType.INCOME);
        assertEquals(1, result.size());
    }

    @Test
    void shouldDeleteById() {
        transactionService.delete("8");
        verify(transactionRepository).deleteById("8");
    }
}
