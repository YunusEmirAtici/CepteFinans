package com.finanscepte.transaction;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.finanscepte.transaction.controller.TransactionController;
import com.finanscepte.transaction.dto.TransactionResponse;
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
class TransactionServiceApplicationTest_20260410 {

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void shouldReturnFilteredListFromController() {
        TransactionResponse response = new TransactionResponse(
                "1", "u1", BigDecimal.ONE, TransactionType.EXPENSE, "food", LocalDate.now(), null, false
        );
        when(transactionService.getByUserIdAndType("u1", TransactionType.EXPENSE)).thenReturn(List.of());
        List<TransactionResponse> result = transactionController.getAll("u1", TransactionType.EXPENSE);
        assertTrue(result.isEmpty());
    }
}
