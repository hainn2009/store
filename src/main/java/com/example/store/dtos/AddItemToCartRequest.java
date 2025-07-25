package com.example.store.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotNull(message = "product id cannot be null")
    private Long productId;
}
