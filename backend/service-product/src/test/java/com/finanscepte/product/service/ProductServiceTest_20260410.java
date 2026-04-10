package com.finanscepte.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.finanscepte.product.exception.ResourceNotFoundException;
import com.finanscepte.product.model.Product;
import com.finanscepte.product.repository.ProductRepository;
import com.finanscepte.product.service.impl.ProductServiceImpl;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest_20260410 {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void shouldCreateProduct() {
        Product request = Product.builder().name("Sigorta").price(BigDecimal.ONE).build();
        Product saved = Product.builder().id("1").name("Sigorta").price(BigDecimal.ONE).build();
        when(productRepository.save(request)).thenReturn(saved);

        Product result = productService.create(request);

        assertEquals("1", result.getId());
    }

    @Test
    void shouldThrowNotFoundWhenProductMissing() {
        when(productRepository.findById("404")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.getById("404"));
    }

    @Test
    void shouldDeleteProductById() {
        productService.delete("9");
        verify(productRepository).deleteById("9");
    }
}
