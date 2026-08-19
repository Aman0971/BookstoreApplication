package org.bookstorebackend.repository;

import org.bookstorebackend.entity.Order;
import org.bookstorebackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
