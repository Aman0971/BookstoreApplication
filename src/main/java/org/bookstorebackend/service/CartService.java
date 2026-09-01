package org.bookstorebackend.service;
import org.bookstorebackend.dto.request.AddMultipleCartRequestDTO;
import org.bookstorebackend.dto.response.CartItemResponseDTO;
import java.util.List;

public interface CartService {
    CartItemResponseDTO addToCart(Long productId);

    List<CartItemResponseDTO> addMultipleToCart(AddMultipleCartRequestDTO request);

    CartItemResponseDTO updateQuantity(Long cartItemId, Integer quantity);
    void removeFromCart(Long cartItemId);
    List<CartItemResponseDTO> getCartItems();

}
