package com.finanscepte.notification.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.finanscepte.notification.model.Notification;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataMongoTest
@Testcontainers
class NotificationRepositoryIT_20260503 {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void shouldPersistAndReadNotification() {
        Notification saved = notificationRepository.save(Notification.builder()
                .userId("u1")
                .type("INFO")
                .message("Test message")
                .read(false)
                .createdAt(LocalDateTime.now())
                .build());

        Notification found = notificationRepository.findById(saved.getId()).orElseThrow();
        assertEquals("Test message", found.getMessage());
        assertEquals("INFO", found.getType());
    }

    @Test
    void shouldFindByUserId() {
        notificationRepository.save(Notification.builder().userId("u1").type("INFO").message("M1").read(false).createdAt(LocalDateTime.now()).build());
        notificationRepository.save(Notification.builder().userId("u1").type("WARNING").message("M2").read(false).createdAt(LocalDateTime.now()).build());
        notificationRepository.save(Notification.builder().userId("u2").type("INFO").message("M3").read(false).createdAt(LocalDateTime.now()).build());

        List<Notification> found = notificationRepository.findByUserId("u1");
        assertEquals(2, found.size());
    }

    @Test
    void shouldFindUnreadNotifications() {
        notificationRepository.save(Notification.builder().userId("u1").type("INFO").message("Read").read(true).createdAt(LocalDateTime.now()).build());
        notificationRepository.save(Notification.builder().userId("u1").type("WARNING").message("Unread").read(false).createdAt(LocalDateTime.now()).build());

        List<Notification> found = notificationRepository.findByUserIdAndReadFalse("u1");
        assertEquals(1, found.size());
        assertEquals("Unread", found.get(0).getMessage());
    }

    @Test
    void shouldDeleteNotification() {
        Notification saved = notificationRepository.save(Notification.builder()
                .userId("u1")
                .type("INFO")
                .message("DeleteMe")
                .read(false)
                .createdAt(LocalDateTime.now())
                .build());

        notificationRepository.deleteById(saved.getId());
        assertTrue(notificationRepository.findById(saved.getId()).isEmpty());
    }
}
