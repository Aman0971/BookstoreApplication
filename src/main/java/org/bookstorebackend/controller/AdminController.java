package org.bookstorebackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookstorebackend.dto.request.AdminLoginRequestDTO;
import org.bookstorebackend.dto.request.AdminRegistrationRequestDTO;
import org.bookstorebackend.dto.response.LoginResponseDTO;
import org.bookstorebackend.dto.response.OrderResponseDTO;
import org.bookstorebackend.dto.response.RegisterResponseDTO;
import org.bookstorebackend.entity.Order;
import org.bookstorebackend.service.AdminService;
import org.bookstorebackend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.bookstorebackend.dto.request.ProductRequestDTO;
import org.bookstorebackend.dto.response.ProductResponseDTO;
import org.bookstorebackend.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ProductService productService;
    private final OrderService orderService;


    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerAdmin(@Valid @RequestBody AdminRegistrationRequestDTO request) {
        RegisterResponseDTO response = adminService.registerAdmin(request);
        return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
    }
    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginAdmin(@Valid @RequestBody AdminLoginRequestDTO request) {
            LoginResponseDTO response = adminService.loginAdmin(request);
            return ResponseEntity.ok(response);
    }

    // ADD BOOK
    @PostMapping("/add/book")
    public ResponseEntity<ProductResponseDTO> addBook(@Valid @RequestBody ProductRequestDTO request) {

        ProductResponseDTO response = productService.addBook(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // UPDATE BOOK    we update quantity of book if the perticular book is out of stock
    @PutMapping("/update/book/{product_id}")
    public ResponseEntity<ProductResponseDTO> updateBook(
            @PathVariable("product_id") Long productId,
            @Valid @RequestBody ProductRequestDTO request) {
        ProductResponseDTO response = productService.updateBook(productId, request);
        return ResponseEntity.ok(response);
    }

    // DELETE BOOK
    @DeleteMapping("/delete/book/{product_id}")
    public ResponseEntity<String> deleteBook(@PathVariable("product_id") Long productId) {

        productService.deleteBook(productId);
        return ResponseEntity.ok("Book deleted successfully");
    }

    //GET All ORDERS
    @GetMapping("/get/orders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(){
        List<OrderResponseDTO>orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}
