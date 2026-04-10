package com.finanscepte.user.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI userOpenApi() {
        return new OpenAPI().info(new Info()
                .title("FinansCepte User Service API")
                .version("v1")
                .description("Kullanici mikroservisi endpoint dokumantasyonu"));
    }
}
