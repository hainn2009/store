package com.example.store.products;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = { "category" })
    List<Product> findByCategoryId(Byte categoryId);

    @EntityGraph(attributePaths = { "category" })
    @Query("SELECT p FROM Product p")
    List<Product> findAllWithCategory();

    // List<Product> findByName(String name);
    // List<Product> findByNameLike(String name);
    // List<Product> findByPriceBetweenOrderByName(BigDecimal min, BigDecimal max);

    // @Query(@Param("min") BigDecimal min, @Param("max") BigDecimal max, value =
    // "select * from products p where p.price between :min and :max order by
    // p.name", nativeQuery = true)
    // List<Product> findProducts(BigDecimal min, BigDecimal max);

    // @Query("select p from Product p join p.category c where p.price between :min
    // and :max order by p.name")
    // List<Product> findProducts(@Param("min") BigDecimal min, @Param("max")
    // BigDecimal max);

    // @Procedure("find_products_by_price")
    // List<Product> findProducts(BigDecimal min, BigDecimal max);

    // @Query("select count(*) from Product p where p.price between :min and :max")
    // List<Product> countProducts(@Param("min") BigDecimal min, @Param("max")
    // BigDecimal max);

    // @Modifying
    // @Query("update Product p set p.price = :price where p.category.id =
    // :categoryId")
    // void updatePriceByCategory(@Param("price") BigDecimal price,
    // @Param("categoryId") Byte categoryId);

    // List<ProductSummary> findByCategory(Category category);
    // @Query("select p.id, p.name from Product p where p.category = :category")
    // List<ProductSummary> findByCategory(@Param("category") Category category);

}
