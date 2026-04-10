package com.finanscepte.gateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthStrategy implements AuthStrategy {

    private final String expectedApiKey;

    public ApiKeyAuthStrategy(@Value("${security.api-key:dev-key}") String expectedApiKey) {
        this.expectedApiKey = expectedApiKey;
    }

    @Override
    public boolean supports(String mode) {
        return "api-key".equalsIgnoreCase(mode);
    }

    @Override
    public boolean isAuthorized(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst("X-API-KEY");
        return expectedApiKey.equals(header);
    }
}
