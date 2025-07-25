package com.example.store.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.store.entities.Cart;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}
