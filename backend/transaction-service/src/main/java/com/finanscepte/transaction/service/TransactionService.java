package com.finanscepte.transaction.service;

import com.finanscepte.common.service.GenericService;
import com.finanscepte.transaction.model.Transaction;
import com.finanscepte.transaction.model.TransactionType;
import java.util.List;

public interface TransactionService extends GenericService<Transaction, String> {
    List<Transaction> getByUserId(String userId);
    List<Transaction> getByUserIdAndType(String userId, TransactionType type);
}
