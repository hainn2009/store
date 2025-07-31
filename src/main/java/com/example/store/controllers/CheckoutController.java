package com.example.store.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.store.dtos.CheckoutRequest;
import com.example.store.dtos.ErrorDto;
import com.example.store.exceptions.CartEmptyException;
import com.example.store.exceptions.CartNotFoundException;
import com.example.store.services.CheckoutService;
import com.stripe.exception.StripeException;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/checkout")
@AllArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutRequest request) {
        try {
            return ResponseEntity.ok(checkoutService.checkout(request));
        } catch (StripeException ex) {
            System.err.println("Error creating a checkout session: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDto("Error creating a checkout session"));
        }
    }

    @ExceptionHandler({ CartNotFoundException.class, CartEmptyException.class })
    public ResponseEntity<ErrorDto> handlerException(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}
