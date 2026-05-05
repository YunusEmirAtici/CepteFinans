package com.finanscepte.notification.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.finanscepte.notification.dto.NotificationResponse;
import com.finanscepte.notification.model.Notification;
import com.finanscepte.notification.service.NotificationService;
import com.finanscepte.notification.util.NotificationMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest_20260503 {

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    void shouldReturnNotificationList() {
        Notification notification = Notification.builder().id("1").userId("u1").type("INFO").message("Hello").read(false).build();
        NotificationResponse response = new NotificationResponse("1", "u1", "INFO", "Hello", false, null);
        when(notificationService.getAll()).thenReturn(List.of(notification));
        when(notificationMapper.toResponse(notification)).thenReturn(response);

        List<NotificationResponse> notifications = notificationController.getAll();

        assertEquals(1, notifications.size());
    }
}
