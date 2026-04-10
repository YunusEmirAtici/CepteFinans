package com.finanscepte.transaction.controller;

import com.finanscepte.transaction.dto.TransactionRequest;
import com.finanscepte.transaction.dto.TransactionResponse;
import com.finanscepte.transaction.model.TransactionType;
import com.finanscepte.transaction.service.TransactionService;
import com.finanscepte.transaction.util.TransactionMapper;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse create(@Valid @RequestBody TransactionRequest request) {
        return transactionMapper.toResponse(transactionService.create(transactionMapper.toEntity(request)));
    }

    @GetMapping("/{id}")
    public TransactionResponse getById(@PathVariable String id) {
        return transactionMapper.toResponse(transactionService.getById(id));
    }

    @GetMapping
    public List<TransactionResponse> getAll(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) TransactionType type) {
        if (userId != null && type != null) {
            return transactionService.getByUserIdAndType(userId, type).stream().map(transactionMapper::toResponse).toList();
        }
        if (userId != null) {
            return transactionService.getByUserId(userId).stream().map(transactionMapper::toResponse).toList();
        }
        return transactionService.getAll().stream().map(transactionMapper::toResponse).toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        transactionService.delete(id);
    }
}
