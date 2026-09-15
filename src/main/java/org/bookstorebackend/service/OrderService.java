package org.bookstorebackend.service;
import org.bookstorebackend.dto.response.OrderResponseDTO;

import java.util.List;

public interface OrderService {
    OrderResponseDTO addOrder();
    OrderResponseDTO buyNow(Long productId, Integer quantity);
    List<OrderResponseDTO> getAllOrders();
    List<OrderResponseDTO> getMyOrders();
}