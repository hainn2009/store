package com.example.store.payments;

import java.util.Optional;

import com.example.store.entities.Order;

public interface PaymentGateway {
    public CheckoutSession createCheckoutSession(Order order);

    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
