package com.example.store.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private String status;
    private LocalDate createdAt;
    private List<OrderItemDto> items;
    private BigDecimal totalPrice;
}
