package com.finanscepte.subscription.controller;

import com.finanscepte.subscription.dto.SubscriptionRequest;
import com.finanscepte.subscription.dto.SubscriptionResponse;
import com.finanscepte.subscription.service.SubscriptionService;
import com.finanscepte.subscription.util.SubscriptionMapper;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponse create(@Valid @RequestBody SubscriptionRequest request) {
        return subscriptionMapper.toResponse(subscriptionService.create(subscriptionMapper.toEntity(request)));
    }

    @GetMapping("/{id}")
    public SubscriptionResponse getById(@PathVariable String id) {
        return subscriptionMapper.toResponse(subscriptionService.getById(id));
    }

    @GetMapping
    public List<SubscriptionResponse> getAll(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) Boolean active) {
        if (userId != null && active != null) {
            return subscriptionService.getByUserIdAndActive(userId, active).stream().map(subscriptionMapper::toResponse).toList();
        }
        if (userId != null) {
            return subscriptionService.getByUserId(userId).stream().map(subscriptionMapper::toResponse).toList();
        }
        return subscriptionService.getAll().stream().map(subscriptionMapper::toResponse).toList();
    }

    @GetMapping("/upcoming")
    public List<SubscriptionResponse> getUpcomingPayments(
            @RequestParam String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return subscriptionService.getUpcomingPayments(userId, fromDate, toDate).stream()
                .map(subscriptionMapper::toResponse)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        subscriptionService.delete(id);
    }
}
