package com.example.store.users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = { "addresses" })
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = { "addresses" })
    @Query("select u from User u")
    List<User> findAllWithAddresses();

    boolean existsByEmail(String email);
}
