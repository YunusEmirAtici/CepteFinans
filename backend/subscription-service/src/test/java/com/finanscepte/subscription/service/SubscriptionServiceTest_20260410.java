package com.finanscepte.subscription.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.finanscepte.common.exception.ResourceNotFoundException;
import com.finanscepte.subscription.model.BillingCycle;
import com.finanscepte.subscription.model.Subscription;
import com.finanscepte.subscription.repository.SubscriptionRepository;
import com.finanscepte.subscription.service.impl.SubscriptionServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest_20260410 {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    @Test
    void shouldReturnSubscriptionById() {
        Subscription subscription = Subscription.builder()
                .id("1")
                .userId("u1")
                .name("Netflix")
                .amount(BigDecimal.TEN)
                .billingCycle(BillingCycle.MONTHLY)
                .startDate(LocalDate.now())
                .nextPaymentDate(LocalDate.now().plusDays(10))
                .category("entertainment")
                .active(true)
                .build();
        when(subscriptionRepository.findById("1")).thenReturn(Optional.of(subscription));
        Subscription result = subscriptionService.getById("1");
        assertEquals("Netflix", result.getName());
    }

    @Test
    void shouldThrowWhenMissing() {
        when(subscriptionRepository.findById("404")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> subscriptionService.getById("404"));
    }

    @Test
    void shouldReturnUpcomingPayments() {
        when(subscriptionRepository.findByUserIdAndNextPaymentDateBetween("u1", LocalDate.now(), LocalDate.now().plusDays(7)))
                .thenReturn(List.of(Subscription.builder().id("x").build()));
        List<Subscription> result = subscriptionService.getUpcomingPayments("u1", LocalDate.now(), LocalDate.now().plusDays(7));
        assertEquals(1, result.size());
    }

    @Test
    void shouldDeleteById() {
        subscriptionService.delete("9");
        verify(subscriptionRepository).deleteById("9");
    }
}
