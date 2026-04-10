package com.finanscepte.product.service.impl;

import com.finanscepte.product.exception.ResourceNotFoundException;
import com.finanscepte.product.model.Product;
import com.finanscepte.product.repository.ProductRepository;
import com.finanscepte.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product create(Product entity) {
        return productRepository.save(entity);
    }

    @Override
    public Product getById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public void delete(String id) {
        productRepository.deleteById(id);
    }
}
