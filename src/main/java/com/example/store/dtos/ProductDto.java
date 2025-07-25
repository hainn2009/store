package com.example.store.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Byte categoryId;
}
