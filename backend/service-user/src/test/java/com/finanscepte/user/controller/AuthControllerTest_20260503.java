package com.finanscepte.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.finanscepte.user.dto.AuthRequest;
import com.finanscepte.user.dto.AuthResponse;
import com.finanscepte.user.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest_20260503 {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void shouldLoginSuccessfully() {
        AuthRequest request = new AuthRequest("test@test.com", "password");
        AuthResponse expected = new AuthResponse("jwt-token-123", "Bearer");
        when(authService.login(request)).thenReturn(expected);

        AuthResponse result = authController.login(request);

        assertNotNull(result);
        assertEquals("jwt-token-123", result.token());
        assertEquals("Bearer", result.type());
    }
}
