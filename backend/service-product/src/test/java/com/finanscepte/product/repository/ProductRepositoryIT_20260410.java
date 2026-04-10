package com.finanscepte.product.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.finanscepte.product.model.Product;
import java.math.BigDecimal;
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
class ProductRepositoryIT_20260410 {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldPersistAndReadProduct() {
        Product saved = productRepository.save(Product.builder().name("Kredi").price(BigDecimal.valueOf(99)).build());
        Product found = productRepository.findById(saved.getId()).orElseThrow();
        assertEquals("Kredi", found.getName());
    }
}
