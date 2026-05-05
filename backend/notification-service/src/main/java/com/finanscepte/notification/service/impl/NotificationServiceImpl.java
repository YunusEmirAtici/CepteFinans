package com.finanscepte.notification.service.impl;

import com.finanscepte.common.exception.ResourceNotFoundException;
import com.finanscepte.notification.event.AutoCreateNotificationListener;
import com.finanscepte.notification.event.NotificationEvent;
import com.finanscepte.notification.event.NotificationEventPublisher;
import com.finanscepte.notification.model.Notification;
import com.finanscepte.notification.repository.NotificationRepository;
import com.finanscepte.notification.service.NotificationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationEventPublisher eventPublisher;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   NotificationEventPublisher eventPublisher,
                                   AutoCreateNotificationListener autoListener) {
        this.notificationRepository = notificationRepository;
        this.eventPublisher = eventPublisher;
        this.eventPublisher.addListener(autoListener);
    }

    @Override
    public Notification create(Notification entity) {
        Notification saved = notificationRepository.save(entity);
        eventPublisher.publish(new NotificationEvent(
                saved.getUserId(),
                saved.getType(),
                "Bildirim olusturuldu: " + saved.getMessage(),
                saved.getCreatedAt()
        ));
        return saved;
    }

    @Override
    public Notification getById(String id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));
    }

    @Override
    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    @Override
    public void delete(String id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public List<Notification> findByUserId(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    @Override
    public List<Notification> getUnreadNotifications(String userId) {
        return notificationRepository.findByUserIdAndReadFalse(userId);
    }

    @Override
    public Notification markAsRead(String id) {
        Notification notification = getById(id);
        notification.setRead(true);
        Notification saved = notificationRepository.save(notification);
        eventPublisher.publish(new NotificationEvent(
                saved.getUserId(),
                "READ",
                "Bildirim okundu olarak isaretlendi: " + saved.getMessage(),
                saved.getCreatedAt()
        ));
        return saved;
    }
}
