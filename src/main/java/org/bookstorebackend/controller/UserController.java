package org.bookstorebackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookstorebackend.dto.request.LoginRequestDTO;
import org.bookstorebackend.dto.request.RegisterRequestDTO;
import org.bookstorebackend.dto.response.LoginResponseDTO;
import org.bookstorebackend.dto.response.RegisterResponseDTO;
import org.bookstorebackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
}
