package com.finanscepte.notification.event;

import java.time.LocalDateTime;

public record NotificationEvent(
        String userId,
        String type,
        String message,
        LocalDateTime timestamp
) {
    public NotificationEvent {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
