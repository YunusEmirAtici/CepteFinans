package com.finanscepte.product.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.finanscepte.product.dto.ProductResponse;
import com.finanscepte.product.model.Product;
import com.finanscepte.product.service.ProductService;
import com.finanscepte.product.util.ProductMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest_20260410 {

    @Mock
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductController productController;

    @Test
    void shouldReturnProductList() {
        Product product = Product.builder().id("1").name("Kart Aidati").price(BigDecimal.TEN).build();
        ProductResponse response = new ProductResponse("1", "Kart Aidati", BigDecimal.TEN);
        when(productService.getAll()).thenReturn(List.of(product));
        when(productMapper.toResponse(product)).thenReturn(response);

        List<ProductResponse> products = productController.getAll();

        assertEquals(1, products.size());
    }
}
