package com.finanscepte.product.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI productOpenApi() {
        return new OpenAPI().info(new Info()
                .title("FinansCepte Product Service API")
                .version("v1")
                .description("Urun mikroservisi endpoint dokumantasyonu"));
    }
}
