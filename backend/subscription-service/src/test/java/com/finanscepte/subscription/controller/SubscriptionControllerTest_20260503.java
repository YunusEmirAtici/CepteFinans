package com.finanscepte.subscription.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.finanscepte.subscription.dto.SubscriptionRequest;
import com.finanscepte.subscription.dto.SubscriptionResponse;
import com.finanscepte.subscription.model.BillingCycle;
import com.finanscepte.subscription.model.Subscription;
import com.finanscepte.subscription.service.SubscriptionService;
import com.finanscepte.subscription.util.SubscriptionMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubscriptionControllerTest_20260503 {

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionMapper subscriptionMapper;

    @InjectMocks
    private SubscriptionController subscriptionController;

    @Test
    void shouldReturnSubscriptionList() {
        Subscription sub = Subscription.builder().id("1").userId("u1").name("Netflix").amount(BigDecimal.TEN).billingCycle(BillingCycle.MONTHLY).nextBillingDate(LocalDate.now()).active(true).build();
        SubscriptionResponse response = new SubscriptionResponse("1", "u1", "Netflix", BigDecimal.TEN, BillingCycle.MONTHLY, LocalDate.now(), true);
        when(subscriptionService.getAll()).thenReturn(List.of(sub));
        when(subscriptionMapper.toResponse(sub)).thenReturn(response);

        List<SubscriptionResponse> result = subscriptionController.getAll(null, null);

        assertEquals(1, result.size());
        assertEquals("Netflix", result.get(0).name());
    }

    @Test
    void shouldCreateSubscription() {
        SubscriptionRequest request = new SubscriptionRequest("u1", "Spotify", BigDecimal.valueOf(50), BillingCycle.MONTHLY, LocalDate.now(), true);
        Subscription entity = Subscription.builder().userId("u1").name("Spotify").amount(BigDecimal.valueOf(50)).billingCycle(BillingCycle.MONTHLY).nextBillingDate(LocalDate.now()).active(true).build();
        Subscription saved = Subscription.builder().id("2").userId("u1").name("Spotify").amount(BigDecimal.valueOf(50)).billingCycle(BillingCycle.MONTHLY).nextBillingDate(LocalDate.now()).active(true).build();
        SubscriptionResponse response = new SubscriptionResponse("2", "u1", "Spotify", BigDecimal.valueOf(50), BillingCycle.MONTHLY, LocalDate.now(), true);

        when(subscriptionMapper.toEntity(request)).thenReturn(entity);
        when(subscriptionService.create(entity)).thenReturn(saved);
        when(subscriptionMapper.toResponse(saved)).thenReturn(response);

        SubscriptionResponse result = subscriptionController.create(request);

        assertNotNull(result);
        assertEquals("2", result.id());
    }

    @Test
    void shouldFilterByUserIdAndActive() {
        Subscription sub = Subscription.builder().id("1").userId("u1").name("Netflix").amount(BigDecimal.TEN).billingCycle(BillingCycle.MONTHLY).nextBillingDate(LocalDate.now()).active(true).build();
        SubscriptionResponse response = new SubscriptionResponse("1", "u1", "Netflix", BigDecimal.TEN, BillingCycle.MONTHLY, LocalDate.now(), true);
        when(subscriptionService.getByUserIdAndActive("u1", true)).thenReturn(List.of(sub));
        when(subscriptionMapper.toResponse(sub)).thenReturn(response);

        List<SubscriptionResponse> result = subscriptionController.getAll("u1", true);

        assertEquals(1, result.size());
    }

    @Test
    void shouldDeleteSubscription() {
        subscriptionController.delete("9");
        verify(subscriptionService).delete("9");
    }
}
