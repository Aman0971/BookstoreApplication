package org.bookstorebackend.service.Impl;

import lombok.RequiredArgsConstructor;
import org.bookstorebackend.dto.response.WishlistItemResponseDTO;
import org.bookstorebackend.entity.Product;
import org.bookstorebackend.entity.User;
import org.bookstorebackend.entity.Wishlist;
import org.bookstorebackend.exception.ResourceNotFoundException;
import org.bookstorebackend.repository.ProductRepository;
import org.bookstorebackend.repository.UserRepository;
import org.bookstorebackend.repository.WishlistRepository;
import org.bookstorebackend.service.WishlistService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class WishlistServiceImpl implements WishlistService {

        private final WishlistRepository wishlistRepository;
        private final ProductRepository productRepository;
        private final UserRepository userRepository;


        private User getCurrentUser() {

            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            String email = authentication.getName();

            return userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("User not found"));
        }


        @Override
        public WishlistItemResponseDTO addToWishlist(Long productId) {

            User user = getCurrentUser();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found"));

            if (wishlistRepository.existsByUserAndProductId(user, productId)) {
                throw new RuntimeException("Product already exists in wishlist");
            }

            Wishlist wishlist = Wishlist.builder()
                    .user(user)
                    .product(product)
                    .build();

            wishlistRepository.save(wishlist);

            return WishlistItemResponseDTO.builder()
                    .productId(product.getId())
                    .bookName(product.getBookName())
                    .author(product.getAuthor())
                    .price(product.getPrice())
                    .bookImage(product.getBookImage())
                    .build();
        }


        @Override
        public void removeFromWishlist(Long productId) {

            User user = getCurrentUser();

            Wishlist wishlist = wishlistRepository
                    .findByUserAndProductId(user, productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found in wishlist"));

            wishlistRepository.delete(wishlist);
        }


        @Override
        public List<WishlistItemResponseDTO> getWishlistItems() {

            User user = getCurrentUser();

            return wishlistRepository.findByUser(user)
                    .stream()
                    .map(wishlist -> {

                        Product product = wishlist.getProduct();

                        return WishlistItemResponseDTO.builder()
                                .productId(product.getId())
                                .bookName(product.getBookName())
                                .author(product.getAuthor())
                                .price(product.getPrice())
                                .bookImage(product.getBookImage())
                                .build();
                    })
                    .toList();
        }
    }
