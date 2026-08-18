package org.bookstorebackend.service;
import org.bookstorebackend.dto.response.CartItemResponseDTO;
import java.util.List;

public interface CartService {
    CartItemResponseDTO addToCart(Long productId);
    CartItemResponseDTO updateQuantity(Long cartItemId, Integer quantity);
    void removeFromCart(Long cartItemId);
    List<CartItemResponseDTO> getCartItems();
}
