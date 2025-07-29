package com.example.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.store.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
