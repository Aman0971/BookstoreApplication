package org.bookstorebackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookstorebackend.dto.request.ForgotPasswordRequestDTO;
import org.bookstorebackend.dto.request.ResetPasswordRequestDTO;
import org.bookstorebackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PasswordController {
    private final UserService userService;
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO dto) {
        userService.forgotPassword(dto);
        return ResponseEntity.ok("OTP sent successfully to your email");
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO dto) {

        userService.resetPassword(dto);
        return ResponseEntity.ok("Password reset successfully");
    }
}
