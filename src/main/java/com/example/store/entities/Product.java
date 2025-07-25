package com.example.store.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

    @ManyToMany(mappedBy = "favoriteProducts")
    @ToString.Exclude
    private List<User> users = new ArrayList<>();

    public Product(String name, BigDecimal price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public void addCategory(Category category) {
        this.category = category;
        category.getProducts().add(this);
    }

    public void removeCategory(Category category) {
        this.category = null;
        category.getProducts().remove(this);
    }
}
