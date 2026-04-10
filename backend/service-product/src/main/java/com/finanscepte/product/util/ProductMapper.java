package com.finanscepte.product.util;

import com.finanscepte.product.dto.ProductRequest;
import com.finanscepte.product.dto.ProductResponse;
import com.finanscepte.product.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        return Product.builder()
                .name(request.name())
                .price(request.price())
                .build();
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }
}
