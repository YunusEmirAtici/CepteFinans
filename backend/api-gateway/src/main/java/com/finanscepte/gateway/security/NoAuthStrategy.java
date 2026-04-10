package com.finanscepte.gateway.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class NoAuthStrategy implements AuthStrategy {
    @Override
    public boolean supports(String mode) {
        return "none".equalsIgnoreCase(mode);
    }

    @Override
    public boolean isAuthorized(ServerHttpRequest request) {
        return true;
    }
}
