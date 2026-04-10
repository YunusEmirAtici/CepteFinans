package com.finanscepte.user.service;

import com.finanscepte.user.dto.AuthRequest;
import com.finanscepte.user.dto.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);
}
