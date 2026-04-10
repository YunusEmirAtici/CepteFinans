package com.finanscepte.user.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.finanscepte.user.model.User;
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
class UserMongoRepositoryIT_20260410 {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private UserMongoRepository userMongoRepository;

    @Test
    void shouldPersistAndReadUser() {
        User saved = userMongoRepository.save(User.builder().username("test").email("test@x.com").build());
        User found = userMongoRepository.findById(saved.getId()).orElseThrow();
        assertEquals("test", found.getUsername());
    }
}
