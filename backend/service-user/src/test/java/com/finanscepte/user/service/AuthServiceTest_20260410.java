package com.finanscepte.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.finanscepte.user.dto.AuthRequest;
import com.finanscepte.user.dto.AuthResponse;
import com.finanscepte.user.security.JwtService;
import com.finanscepte.user.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest_20260410 {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldGenerateBearerTokenOnLogin() {
        when(jwtService.generateToken("test@mail.com")).thenReturn("mock-token");
        AuthResponse response = authService.login(new AuthRequest("test@mail.com", "123456"));
        assertNotNull(response.accessToken());
        assertEquals("Bearer", response.tokenType());
    }
}
