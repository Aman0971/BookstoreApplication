package org.bookstorebackend.service.Impl;

import lombok.RequiredArgsConstructor;
import org.bookstorebackend.dto.request.AdminLoginRequestDTO;
import org.bookstorebackend.dto.request.AdminRegistrationRequestDTO;
import org.bookstorebackend.dto.response.LoginResponseDTO;
import org.bookstorebackend.dto.response.RegisterResponseDTO;
import org.bookstorebackend.entity.User;
import org.bookstorebackend.repository.UserRepository;
import org.bookstorebackend.service.AdminService;
import org.bookstorebackend.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class AdminServiceImpl implements AdminService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;

        @Override
        public RegisterResponseDTO registerAdmin(
                AdminRegistrationRequestDTO request) {

            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new RuntimeException("Email already registered");
            }

            User user = new User();

            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());

            user.setPassword(passwordEncoder.encode(request.getPassword()));

            // IMPORTANT
            user.setRole(User.Role.ADMIN);

            User savedUser = userRepository.save(user);

            return RegisterResponseDTO.builder()
                    .id(savedUser.getId())
                    .firstName(savedUser.getFirstName())
                    .lastName(savedUser.getLastName())
                    .email(savedUser.getEmail())
                    .role(savedUser.getRole().name())
                    .build();
        }

        @Override
        public LoginResponseDTO loginAdmin(
                AdminLoginRequestDTO request) {

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() ->
                            new RuntimeException("Invalid email or password"));

            if (user.getRole() != User.Role.ADMIN) {
                throw new RuntimeException("Access denied");
            }

            if (!passwordEncoder.matches(
                    request.getPassword(),
                    user.getPassword())) {

                throw new RuntimeException("Invalid email or password");
            }
            String token = jwtUtil.generateToken(user);

            return LoginResponseDTO.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .token(token)
                    .build();
        }
    }
