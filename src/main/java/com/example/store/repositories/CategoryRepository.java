package com.example.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.store.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Byte> {

}