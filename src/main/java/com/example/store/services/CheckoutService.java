package com.example.store.services;

import org.springframework.stereotype.Service;

import com.example.store.dtos.CheckoutRequest;
import com.example.store.dtos.CheckoutResponse;
import com.example.store.entities.Order;
import com.example.store.entities.OrderItem;
import com.example.store.entities.OrderStatus;
import com.example.store.exceptions.CartEmptyException;
import com.example.store.exceptions.CartNotFoundException;
import com.example.store.repositories.CartRepository;
import com.example.store.repositories.OrderRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final AuthService authService;
    private final OrderRepository orderRepository;

    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(authService.getCurrentUser());

        cart.getItems().forEach(item -> {
            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            order.getItems().add(orderItem);
        });

        orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return new CheckoutResponse(order.getId());
    }
}
