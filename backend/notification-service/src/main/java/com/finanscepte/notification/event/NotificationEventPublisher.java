package com.finanscepte.notification.event;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventPublisher {

    private final List<NotificationEventListener> listeners = new ArrayList<>();

    public void addListener(NotificationEventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(NotificationEventListener listener) {
        listeners.remove(listener);
    }

    public void publish(NotificationEvent event) {
        for (NotificationEventListener listener : listeners) {
            listener.onNotificationEvent(event);
        }
    }
}
