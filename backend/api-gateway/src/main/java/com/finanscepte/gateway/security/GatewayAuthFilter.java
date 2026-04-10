package com.finanscepte.gateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;

@Component
public class GatewayAuthFilter implements GlobalFilter, Ordered {

    private final AuthStrategyFactory authStrategyFactory;
    private final String authMode;

    public GatewayAuthFilter(AuthStrategyFactory authStrategyFactory,
                             @Value("${security.auth-mode:none}") String authMode) {
        this.authStrategyFactory = authStrategyFactory;
        this.authMode = authMode;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getPath().value().startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        boolean authorized = authStrategyFactory.get(authMode).isAuthorized(exchange.getRequest());
        if (authorized) {
            return chain.filter(exchange);
        }
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
