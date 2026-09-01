package org.bookstorebackend.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bookstorebackend.dto.request.AddMultipleCartRequestDTO;
import org.bookstorebackend.dto.request.CartItemRequestDTO;
import org.bookstorebackend.dto.response.CartItemResponseDTO;
import org.bookstorebackend.entity.CartItem;
import org.bookstorebackend.entity.Product;
import org.bookstorebackend.entity.User;
import org.bookstorebackend.exception.ResourceNotFoundException;
import org.bookstorebackend.repository.CartItemRepository;
import org.bookstorebackend.repository.ProductRepository;
import org.bookstorebackend.repository.UserRepository;
import org.bookstorebackend.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class CartServiceImpl implements CartService {

        private final CartItemRepository cartItemRepository;
        private final ProductRepository productRepository;
        private final UserRepository userRepository;

        @Override
        public CartItemResponseDTO addToCart(Long productId) {

            User user = getLoggedInUser();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found with id: " + productId));

            if (product.getQuantity() <= 0) {
                throw new RuntimeException("Product is out of stock");
            }

            CartItem cartItem = cartItemRepository
                    .findByUserAndProduct(user, product)
                    .orElse(null);

            if (cartItem != null) {

                if (cartItem.getQuantity() >= product.getQuantity()) {
                    throw new RuntimeException("Requested quantity is not available");
                }

                cartItem.setQuantity(cartItem.getQuantity() + 1);

            } else {

                cartItem = CartItem.builder()
                        .user(user)
                        .product(product)
                        .quantity(1)
                        .build();
            }

            CartItem savedCartItem = cartItemRepository.save(cartItem);

            return mapToResponse(savedCartItem);
        }

        @Override
        @Transactional
        public List<CartItemResponseDTO> addMultipleToCart(AddMultipleCartRequestDTO request) {

            User user = getLoggedInUser();

            List<CartItemResponseDTO> responseList = new ArrayList<>();

            for (CartItemRequestDTO item : request.getItems()) {

                Long productId = item.getProductId();
                Integer requestedQuantity = item.getQuantity();

                Product product = productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found with id: " + productId));

                if (product.getQuantity() <= 0) {
                    throw new RuntimeException(
                            "Product is out of stock: " + product.getBookName());
                }

                CartItem cartItem = cartItemRepository
                        .findByUserAndProduct(user, product)
                        .orElse(null);

                if (cartItem != null) {

                    int newQuantity =
                            cartItem.getQuantity() + requestedQuantity;

                    if (newQuantity > product.getQuantity()) {
                        throw new RuntimeException(
                                "Requested quantity is not available for product: "
                                        + product.getBookName());
                    }

                    cartItem.setQuantity(newQuantity);

                } else {

                    if (requestedQuantity > product.getQuantity()) {
                        throw new RuntimeException(
                                "Requested quantity is not available for product: "
                                        + product.getBookName());
                    }

                    cartItem = CartItem.builder()
                            .user(user)
                            .product(product)
                            .quantity(requestedQuantity)
                            .build();
                }

                CartItem savedCartItem =
                        cartItemRepository.save(cartItem);

                responseList.add(mapToResponse(savedCartItem));
            }

            return responseList;
        }

        @Override
        public CartItemResponseDTO updateQuantity(Long cartItemId, Integer quantity) {

            if (quantity == null || quantity <= 0) {
                throw new RuntimeException("Quantity must be greater than zero");
            }

            User user = getLoggedInUser();

            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Cart item not found with id: " + cartItemId));

            if (!cartItem.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("You cannot update another user's cart");
            }

            Product product = cartItem.getProduct();

            if (quantity > product.getQuantity()) {
                throw new RuntimeException("Requested quantity is not available");
            }

            cartItem.setQuantity(quantity);

            CartItem updatedCartItem = cartItemRepository.save(cartItem);

            return mapToResponse(updatedCartItem);
        }

        @Override
        public void removeFromCart(Long cartItemId) {

            User user = getLoggedInUser();

            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Cart item not found with id: " + cartItemId));

            if (!cartItem.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("You cannot delete another user's cart item");
            }

            cartItemRepository.delete(cartItem);
        }

        @Override
        public List<CartItemResponseDTO> getCartItems() {

            User user = getLoggedInUser();

            return cartItemRepository.findByUser(user)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        private User getLoggedInUser() {

            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            String email = authentication.getName();

            return userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("User not found"));
        }

        private CartItemResponseDTO mapToResponse(CartItem cartItem) {

            Product product = cartItem.getProduct();

            return CartItemResponseDTO.builder()
                    .id(cartItem.getId())
                    .productId(product.getId())
                    .author(product.getAuthor())
                    .bookName(product.getBookName())
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .totalPrice(product.getPrice() * cartItem.getQuantity())
                    .build();
        }


    }
