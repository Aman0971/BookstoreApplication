package org.bookstorebackend.controller;


import lombok.RequiredArgsConstructor;
import org.bookstorebackend.dto.response.CartItemResponseDTO;
import org.bookstorebackend.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add-cart/{productId}")
    public ResponseEntity<CartItemResponseDTO> addToCart(@PathVariable Long productId) {

        return ResponseEntity.ok(cartService.addToCart(productId)
        );
    }

    @PutMapping("/cart-quantity/{cartItemId}")
    public ResponseEntity<CartItemResponseDTO> updateQuantity(
                @PathVariable Long cartItemId,
                @RequestParam Integer quantity) {  //we use this annotation , it helps to write quantity after ? in this api

            return ResponseEntity.ok(cartService.updateQuantity(cartItemId, quantity)
            );
    }

    @DeleteMapping("/remove-cart/{cartItemId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long cartItemId) {

            cartService.removeFromCart(cartItemId);
            return ResponseEntity.ok("Cart item removed successfully");
    }

    @GetMapping("/get-cart")
    public ResponseEntity<List<CartItemResponseDTO>> getCartItems() {
            return ResponseEntity.ok(cartService.getCartItems()
            );
    }
}
