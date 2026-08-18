package org.bookstorebackend.repository;

import org.bookstorebackend.entity.CartItem;
import org.bookstorebackend.entity.Product;
import org.bookstorebackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    List<CartItem> findByUser(User user);
}
