package com.example.store.dtos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class CartDto {
    private UUID id;
    // Need to check List or Set here
    private List<CartItemDto> items = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
