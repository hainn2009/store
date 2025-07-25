package com.example.store.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.store.dtos.ProductDto;
import com.example.store.entities.Product;
import com.example.store.mappers.ProductMapper;
import com.example.store.repositories.CategoryRepository;
import com.example.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@RequestMapping("/products")
@AllArgsConstructor
@RestController
public class ProductController {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public Iterable<ProductDto> getAllProducts(
        @RequestParam(required = false, defaultValue = "", name = "categoryId") Byte categoryId
    ) {
        List<Product> products = categoryId == null ? productRepository.findAllWithCategory() : productRepository.findByCategoryId(categoryId);
        return products.stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable("id") Long id) {
        Product existProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toDto(existProduct);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto, UriComponentsBuilder uriBuilder) {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        var product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();

        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDto productDto) {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        productMapper.update(productDto, product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(product);

        return ResponseEntity.noContent().build();
    }
}
