package com.finanscepte.subscription;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.finanscepte.subscription.controller.SubscriptionController;
import com.finanscepte.subscription.service.SubscriptionService;
import com.finanscepte.subscription.util.SubscriptionMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceApplicationTest_20260410 {

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionMapper subscriptionMapper;

    @InjectMocks
    private SubscriptionController subscriptionController;

    @Test
    void shouldReturnEmptyListWhenServiceReturnsEmpty() {
        when(subscriptionService.getByUserId("u1")).thenReturn(List.of());
        assertTrue(subscriptionController.getAll("u1", null).isEmpty());
    }
}
