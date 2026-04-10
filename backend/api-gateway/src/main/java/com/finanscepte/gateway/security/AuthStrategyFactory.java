package com.finanscepte.gateway.security;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AuthStrategyFactory {

    private final List<AuthStrategy> strategies;

    public AuthStrategyFactory(List<AuthStrategy> strategies) {
        this.strategies = strategies;
    }

    public AuthStrategy get(String mode) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(mode))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported auth mode: " + mode));
    }
}
