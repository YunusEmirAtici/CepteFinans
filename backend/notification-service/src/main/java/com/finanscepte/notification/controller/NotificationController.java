package com.finanscepte.notification.controller;

import com.finanscepte.notification.dto.NotificationRequest;
import com.finanscepte.notification.dto.NotificationResponse;
import com.finanscepte.notification.service.NotificationService;
import com.finanscepte.notification.util.NotificationMapper;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponse create(@Valid @RequestBody NotificationRequest request) {
        return notificationMapper.toResponse(notificationService.create(notificationMapper.toEntity(request)));
    }

    @GetMapping("/{id}")
    public NotificationResponse getById(@PathVariable String id) {
        return notificationMapper.toResponse(notificationService.getById(id));
    }

    @GetMapping
    public List<NotificationResponse> getAll() {
        return notificationService.getAll().stream().map(notificationMapper::toResponse).toList();
    }

    @GetMapping("/user/{userId}")
    public List<NotificationResponse> getByUserId(@PathVariable String userId) {
        return notificationService.findByUserId(userId).stream().map(notificationMapper::toResponse).toList();
    }

    @GetMapping("/user/{userId}/unread")
    public List<NotificationResponse> getUnread(@PathVariable String userId) {
        return notificationService.getUnreadNotifications(userId)
                .stream().map(notificationMapper::toResponse).toList();
    }

    @PatchMapping("/{id}/read")
    public NotificationResponse markAsRead(@PathVariable String id) {
        return notificationMapper.toResponse(notificationService.markAsRead(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        notificationService.delete(id);
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("service", "notification-service", "status", "ok");
    }
}
