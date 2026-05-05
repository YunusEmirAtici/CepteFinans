package com.finanscepte.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.finanscepte.user.dto.UserRequest;
import com.finanscepte.user.dto.UserResponse;
import com.finanscepte.user.model.User;
import com.finanscepte.user.service.UserService;
import com.finanscepte.user.util.UserMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserControllerTest_20260503 {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Test
    void shouldReturnUserList() {
        User user = User.builder().id("1").username("testuser").email("test@test.com").build();
        UserResponse response = new UserResponse("1", "testuser", "test@test.com");
        when(userService.getAll()).thenReturn(List.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        List<UserResponse> users = userController.getAll();

        assertEquals(1, users.size());
        assertEquals("testuser", users.get(0).username());
    }

    @Test
    void shouldCreateUser() {
        UserRequest request = new UserRequest("newuser", "new@test.com");
        User entity = User.builder().username("newuser").email("new@test.com").build();
        User saved = User.builder().id("2").username("newuser").email("new@test.com").build();
        UserResponse response = new UserResponse("2", "newuser", "new@test.com");

        when(userMapper.toEntity(request)).thenReturn(entity);
        when(userService.create(entity)).thenReturn(saved);
        when(userMapper.toResponse(saved)).thenReturn(response);

        UserResponse result = userController.create(request);

        assertNotNull(result);
        assertEquals("2", result.id());
    }

    @Test
    void shouldGetUserById() {
        User user = User.builder().id("3").username("findme").email("find@test.com").build();
        UserResponse response = new UserResponse("3", "findme", "find@test.com");
        when(userService.getById("3")).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(response);

        UserResponse result = userController.getById("3");

        assertEquals("findme", result.username());
    }

    @Test
    void shouldDeleteUser() {
        userController.delete("9");
        verify(userService).delete("9");
    }
}
