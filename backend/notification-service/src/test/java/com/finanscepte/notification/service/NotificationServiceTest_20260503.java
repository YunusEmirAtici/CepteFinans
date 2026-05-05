package com.finanscepte.notification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.finanscepte.common.exception.ResourceNotFoundException;
import com.finanscepte.notification.model.Notification;
import com.finanscepte.notification.repository.NotificationRepository;
import com.finanscepte.notification.service.impl.NotificationServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest_20260503 {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void shouldCreateNotification() {
        Notification request = Notification.builder().userId("u1").type("INFO").message("Test message").build();
        Notification saved = Notification.builder().id("1").userId("u1").type("INFO").message("Test message").read(false).build();
        when(notificationRepository.save(request)).thenReturn(saved);

        Notification result = notificationService.create(request);

        assertEquals("1", result.getId());
    }

    @Test
    void shouldThrowNotFoundWhenNotificationMissing() {
        when(notificationRepository.findById("404")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> notificationService.getById("404"));
    }

    @Test
    void shouldDeleteNotificationById() {
        notificationService.delete("9");
        verify(notificationRepository).deleteById("9");
    }

    @Test
    void shouldGetUnreadNotifications() {
        Notification notification = Notification.builder().id("1").userId("u1").type("INFO").message("Test").read(false).build();
        when(notificationRepository.findByUserIdAndReadFalse("u1")).thenReturn(List.of(notification));

        List<Notification> result = notificationService.getUnreadNotifications("u1");

        assertEquals(1, result.size());
    }

    @Test
    void shouldMarkAsRead() {
        Notification notification = Notification.builder().id("1").userId("u1").type("INFO").message("Test").read(false).build();
        when(notificationRepository.findById("1")).thenReturn(Optional.of(notification));
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationService.markAsRead("1");

        assertTrue(result.isRead());
    }
}
