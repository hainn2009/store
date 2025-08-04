package com.example.store.payments;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutRequest {
    @NotNull(message = "Cart id is requỉred")
    private UUID cartId;
}
