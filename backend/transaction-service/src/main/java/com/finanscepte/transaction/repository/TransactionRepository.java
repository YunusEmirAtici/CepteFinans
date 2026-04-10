package com.finanscepte.transaction.repository;

import com.finanscepte.transaction.model.Transaction;
import com.finanscepte.transaction.model.TransactionType;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByUserId(String userId);
    List<Transaction> findByUserIdAndType(String userId, TransactionType type);
}
