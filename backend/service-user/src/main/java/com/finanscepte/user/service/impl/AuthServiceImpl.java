package com.finanscepte.user.service.impl;

import com.finanscepte.user.dto.AuthRequest;
import com.finanscepte.user.dto.AuthResponse;
import com.finanscepte.user.service.AuthService;
import com.finanscepte.user.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;

    @Override
    public AuthResponse login(AuthRequest request) {
        String token = jwtService.generateToken(request.email());
        return new AuthResponse(token, "Bearer");
    }
}
