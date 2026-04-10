package com.finanscepte.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "name bos olamaz") String name,
        @DecimalMin(value = "0.0", inclusive = false, message = "price sifirdan buyuk olmali") BigDecimal price
) {
}
