package com.finanscepte.subscription.service.impl;

import com.finanscepte.common.exception.ResourceNotFoundException;
import com.finanscepte.subscription.model.Subscription;
import com.finanscepte.subscription.repository.SubscriptionRepository;
import com.finanscepte.subscription.service.SubscriptionService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription create(Subscription entity) {
        return subscriptionRepository.save(entity);
    }

    @Override
    public Subscription getById(String id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found: " + id));
    }

    @Override
    public List<Subscription> getAll() {
        return subscriptionRepository.findAll();
    }

    @Override
    public void delete(String id) {
        subscriptionRepository.deleteById(id);
    }

    @Override
    public List<Subscription> getByUserId(String userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    @Override
    public List<Subscription> getByUserIdAndActive(String userId, boolean active) {
        return subscriptionRepository.findByUserIdAndActive(userId, active);
    }

    @Override
    public List<Subscription> getUpcomingPayments(String userId, LocalDate from, LocalDate to) {
        return subscriptionRepository.findByUserIdAndNextPaymentDateBetween(userId, from, to);
    }
}
