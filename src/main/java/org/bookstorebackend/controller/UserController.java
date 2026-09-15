package org.bookstorebackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookstorebackend.dto.request.*;
import org.bookstorebackend.dto.response.LoginResponseDTO;
import org.bookstorebackend.dto.response.OrderResponseDTO;
import org.bookstorebackend.dto.response.ProductResponseDTO;
import org.bookstorebackend.dto.response.RegisterResponseDTO;
import org.bookstorebackend.service.OrderService;
import org.bookstorebackend.service.ProductService;
import org.bookstorebackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {

        RegisterResponseDTO response = userService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = userService.login(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/add/Order")
    public ResponseEntity<OrderResponseDTO> addOrder(){
        OrderResponseDTO response = orderService.addOrder();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PostMapping("/buy-now/{productId}")
    public ResponseEntity<OrderResponseDTO> buyNow(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        OrderResponseDTO response = orderService.buyNow(productId, quantity);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders() {

        List<OrderResponseDTO> orders = orderService.getMyOrders();

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/get/books")
    public ResponseEntity<List<ProductResponseDTO>> getAllBooks(){
        return ResponseEntity.ok(productService.getAllBooks());
    }

    @PutMapping("/edit-user")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UpdateUserRequestDTO request) {

        userService.updateUser(request);
        return ResponseEntity.ok("User details updated successfully");
    }
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchBooks(
            @RequestParam String keyword) {

        return ResponseEntity.ok(productService.searchBooks(keyword));
    }
}
