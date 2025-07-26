package com.example.store.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.store.dtos.AddItemToCartRequest;
import com.example.store.dtos.CartDto;
import com.example.store.dtos.CartItemDto;
import com.example.store.entities.Cart;
import com.example.store.entities.CartItem;
import com.example.store.mappers.CartMapper;
import com.example.store.repositories.CartRepository;
import com.example.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
        UriComponentsBuilder uriBuilder
    ) {
        var cart = new Cart();
        cartRepository.save(cart);

        var cartDto = cartMapper.toDto(cart);

        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cart.getId()).toUri();

        // phương án không trả về location
        // return new ResponseEntity<>(carDto, HttpStatus.CREATED);
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
        @PathVariable UUID cartId,
        @RequestBody AddItemToCartRequest request) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }

        var cartItem = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(request.getProductId())).findFirst().orElse(null);
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getItems().add(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }

        cartRepository.save(cart);

        var cartItemDto = cartMapper.toDto(cartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        // var cart = cartRepository.findById(cartId).orElse(null);
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartMapper.toDto(cart));
    }
}
