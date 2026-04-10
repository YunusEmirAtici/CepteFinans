package com.finanscepte.subscription.service;

import com.finanscepte.common.service.GenericService;
import com.finanscepte.subscription.model.Subscription;
import java.time.LocalDate;
import java.util.List;

public interface SubscriptionService extends GenericService<Subscription, String> {
    List<Subscription> getByUserId(String userId);
    List<Subscription> getByUserIdAndActive(String userId, boolean active);
    List<Subscription> getUpcomingPayments(String userId, LocalDate from, LocalDate to);
}
