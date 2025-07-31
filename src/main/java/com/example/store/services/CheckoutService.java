package com.example.store.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.store.dtos.CheckoutRequest;
import com.example.store.dtos.CheckoutResponse;
import com.example.store.entities.Order;
import com.example.store.entities.OrderItem;
import com.example.store.entities.OrderStatus;
import com.example.store.exceptions.CartEmptyException;
import com.example.store.exceptions.CartNotFoundException;
import com.example.store.repositories.CartRepository;
import com.example.store.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;

@Service
// @AllArgsConstructor
// to be able to use @Value
@RequiredArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final AuthService authService;
    private final OrderRepository orderRepository;

    @Value("${websiteUrl}")
    private String webSiteUrl;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) throws StripeException {
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

        try {
            // create a checkout session
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(webSiteUrl + "/checkout-uccess?orderId=" + order.getId())
                    .setCancelUrl(webSiteUrl + "/checkout-cancel");

            order.getItems().forEach(item -> {
                var lineItem = SessionCreateParams.LineItem.builder()
                        .setQuantity(Long.valueOf(item.getQuantity()))
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                // Stripe accepts amounts in cents
                                .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(item.getProduct().getName())
                                        .build())
                                .build())
                        .build();

                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());

            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(), session.getUrl());
        } catch (StripeException ex) {
            orderRepository.delete(order);
            throw ex;
        }
    }
}
