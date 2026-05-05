package com.finanscepte.notification.event;

import com.finanscepte.notification.model.Notification;
import com.finanscepte.notification.service.NotificationService;
import org.springframework.stereotype.Component;

@Component
public class AutoCreateNotificationListener implements NotificationEventListener {

    private final NotificationService notificationService;

    public AutoCreateNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void onNotificationEvent(NotificationEvent event) {
        Notification notification = Notification.builder()
                .userId(event.userId())
                .type(event.type())
                .message(event.message())
                .read(false)
                .createdAt(event.timestamp())
                .build();
        notificationService.create(notification);
    }
}
