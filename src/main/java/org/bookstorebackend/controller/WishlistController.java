package org.bookstorebackend.controller;
import lombok.RequiredArgsConstructor;
import org.bookstorebackend.dto.response.ProductResponseDTO;
import org.bookstorebackend.dto.response.WishlistItemResponseDTO;
import org.bookstorebackend.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/api/user")
    @RequiredArgsConstructor
    public class WishlistController {

        private final WishlistService wishlistService;

        @PostMapping("/add-wishlist/{productId}")
        public ResponseEntity<String> addToWishlist(
                @PathVariable Long productId) {

            wishlistService.addToWishlist(productId);
            return ResponseEntity.ok("Product added to wishlist successfully");
        }

        @DeleteMapping("/remove-wishlist/{productId}")
        public ResponseEntity<String> removeFromWishlist(
                @PathVariable Long productId) {

            wishlistService.removeFromWishlist(productId);
            return ResponseEntity.ok("Product removed from wishlist successfully");
        }

        @GetMapping("/get-wishlist")
        public ResponseEntity<List<WishlistItemResponseDTO>> getWishlistItems() {
            return ResponseEntity.ok(wishlistService.getWishlistItems());
        }
    }
