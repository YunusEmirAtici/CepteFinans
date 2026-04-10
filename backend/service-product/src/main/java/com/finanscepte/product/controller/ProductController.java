package com.finanscepte.product.controller;

import com.finanscepte.product.dto.ProductRequest;
import com.finanscepte.product.dto.ProductResponse;
import com.finanscepte.product.service.ProductService;
import com.finanscepte.product.util.ProductMapper;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return productMapper.toResponse(productService.create(productMapper.toEntity(request)));
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable String id) {
        return productMapper.toResponse(productService.getById(id));
    }

    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.getAll().stream().map(productMapper::toResponse).toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        productService.delete(id);
    }
}
