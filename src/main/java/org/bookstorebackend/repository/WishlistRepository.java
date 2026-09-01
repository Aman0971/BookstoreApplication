package org.bookstorebackend.repository;

import org.bookstorebackend.entity.User;
import org.bookstorebackend.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    List<Wishlist> findByUser(User user);
    Optional<Wishlist>  findByUserAndProductId(User user, Long productId);
    boolean existsByUserAndProductId(User user, Long productId);
}
