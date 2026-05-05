package com.finanscepte.notification.service;

import com.finanscepte.common.service.GenericService;
import com.finanscepte.notification.model.Notification;
import java.util.List;

public interface NotificationService extends GenericService<Notification, String> {
    List<Notification> findByUserId(String userId);
    List<Notification> getUnreadNotifications(String userId);
    Notification markAsRead(String id);
}
