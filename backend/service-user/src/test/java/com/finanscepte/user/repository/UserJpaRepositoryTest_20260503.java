package com.finanscepte.user.repository;

import com.finanscepte.user.model.UserJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserJpaRepositoryTest_20260503 {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    @DisplayName("Should save and retrieve user via JDBC")
    void shouldSaveAndRetrieveUser() {
        // Given
        UserJpaEntity user = UserJpaEntity.builder()
                .username("testuser")
                .email("test@example.com")
                .password("secret")
                .build();

        // When
        UserJpaEntity saved = userJpaRepository.save(user);
        Optional<UserJpaEntity> found = userJpaRepository.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should find user by username")
    void shouldFindByUsername() {
        // Given
        UserJpaEntity user = UserJpaEntity.builder()
                .username("findme")
                .email("find@example.com")
                .password("pass")
                .build();
        userJpaRepository.save(user);

        // When
        Optional<UserJpaEntity> found = userJpaRepository.findByUsername("findme");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("find@example.com");
    }

    @Test
    @DisplayName("Should return empty when user not found")
    void shouldReturnEmptyWhenNotFound() {
        // When
        Optional<UserJpaEntity> found = userJpaRepository.findById(999L);

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should delete user by id")
    void shouldDeleteUser() {
        // Given
        UserJpaEntity user = UserJpaEntity.builder()
                .username("delete")
                .email("del@example.com")
                .password("pass")
                .build();
        UserJpaEntity saved = userJpaRepository.save(user);

        // When
        userJpaRepository.deleteById(saved.getId());
        Optional<UserJpaEntity> found = userJpaRepository.findById(saved.getId());

        // Then
        assertThat(found).isEmpty();
    }
}
