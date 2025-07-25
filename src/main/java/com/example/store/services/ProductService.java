package com.example.store.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.store.entities.Category;
import com.example.store.entities.Product;
import com.example.store.repositories.CategoryRepository;
import com.example.store.repositories.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public void addProduct() {
        var product = new Product("Product 1", new BigDecimal("10.99"), "Description 1");
        var category = new Category("Category 1");
        product.addCategory(category);
        productRepository.save(product);
    }

    @Transactional
    public void addProductToExistingCategory() {
        var product = new Product("Product 2", new BigDecimal("20.99"), "Description 2");
        var category = categoryRepository.findById((byte)3).orElseThrow(() -> new RuntimeException("Category not found"));
        product.addCategory(category);
        productRepository.save(product);
    }
}
