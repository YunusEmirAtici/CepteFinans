package com.finanscepte.subscription.repository;

import com.finanscepte.subscription.model.Subscription;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    List<Subscription> findByUserId(String userId);
    List<Subscription> findByUserIdAndActive(String userId, boolean active);
    List<Subscription> findByUserIdAndNextPaymentDateBetween(String userId, LocalDate from, LocalDate to);
}
