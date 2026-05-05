package com.finanscepte.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.finanscepte.common.exception.ResourceNotFoundException;
import com.finanscepte.user.model.User;
import com.finanscepte.user.repository.UserMongoRepository;
import com.finanscepte.user.service.impl.UserServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest_20260410 {

    @Mock
    private UserMongoRepository userMongoRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldReturnUserWhenFound() {
        User user = User.builder().id("1").username("yunus").email("y@x.com").build();
        when(userMongoRepository.findById("1")).thenReturn(Optional.of(user));

        User result = userService.getById("1");

        assertEquals("yunus", result.getUsername());
    }

    @Test
    void shouldThrowNotFoundWhenUserMissing() {
        when(userMongoRepository.findById("9")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getById("9"));
    }

    @Test
    void shouldDeleteUserById() {
        userService.delete("5");
        verify(userMongoRepository).deleteById("5");
    }
}
