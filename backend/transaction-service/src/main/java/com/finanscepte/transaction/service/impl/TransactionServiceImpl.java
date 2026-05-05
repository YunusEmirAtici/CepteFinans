package com.finanscepte.transaction.service.impl;

import com.finanscepte.common.exception.ResourceNotFoundException;
import com.finanscepte.transaction.model.Transaction;
import com.finanscepte.transaction.model.TransactionType;
import com.finanscepte.transaction.repository.TransactionRepository;
import com.finanscepte.transaction.service.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction create(Transaction entity) {
        return transactionRepository.save(entity);
    }

    @Override
    public Transaction getById(String id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));
    }

    @Override
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    @Override
    public void delete(String id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public List<Transaction> getByUserId(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Override
    public List<Transaction> getByUserIdAndType(String userId, TransactionType type) {
        return transactionRepository.findByUserIdAndType(userId, type);
    }
}
