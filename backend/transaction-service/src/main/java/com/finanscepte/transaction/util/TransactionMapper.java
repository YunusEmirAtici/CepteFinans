package com.finanscepte.transaction.util;

import com.finanscepte.transaction.dto.TransactionRequest;
import com.finanscepte.transaction.dto.TransactionResponse;
import com.finanscepte.transaction.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionRequest request) {
        return Transaction.builder()
                .userId(request.userId())
                .amount(request.amount())
                .type(request.type())
                .category(request.category())
                .transactionDate(request.transactionDate())
                .description(request.description())
                .recurring(request.recurring())
                .build();
    }

    public TransactionResponse toResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getUserId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCategory(),
                transaction.getTransactionDate(),
                transaction.getDescription(),
                transaction.isRecurring()
        );
    }
}
