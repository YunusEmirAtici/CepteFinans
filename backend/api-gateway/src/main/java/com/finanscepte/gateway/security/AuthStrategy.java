package com.finanscepte.gateway.security;

import org.springframework.http.server.reactive.ServerHttpRequest;

public interface AuthStrategy {
    boolean supports(String mode);
    boolean isAuthorized(ServerHttpRequest request);
}
