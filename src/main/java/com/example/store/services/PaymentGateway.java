package com.example.store.services;

import com.example.store.entities.Order;

public interface PaymentGateway {
    public CheckoutSession createCheckoutSession(Order order);
}
