package org.bookstorebackend.service.Impl;
import lombok.RequiredArgsConstructor;
import org.bookstorebackend.dto.request.*;
import org.bookstorebackend.dto.response.LoginResponseDTO;
import org.bookstorebackend.dto.response.RegisterResponseDTO;
import org.bookstorebackend.entity.User;
import org.bookstorebackend.exception.ResourceNotFoundException;
import org.bookstorebackend.mapper.UserMapper;
import org.bookstorebackend.repository.UserRepository;
import org.bookstorebackend.service.EmailService;
import org.bookstorebackend.service.UserService;
import org.bookstorebackend.util.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
    @RequiredArgsConstructor
    public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;
        private final UserMapper userMapper;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;
        private final EmailService emailService;
        private final RedisTemplate<String, Object> redisTemplate;

        @Override
        public RegisterResponseDTO register(RegisterRequestDTO request) {

            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already registered");
            }
            if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                throw new RuntimeException("Phone number already registered");
            }

            User user = userMapper.toEntity(request);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            User savedUser = userRepository.save(user);

            return userMapper.toRegisterResponse(savedUser);
        }

        @Override
        public LoginResponseDTO login(LoginRequestDTO request) {

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() ->
                            new RuntimeException("Invalid email or password"));

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
                    .phoneNumber(user.getPhoneNumber())
                    .role(user.getRole().name())
                    .token(token)
                    .build();
        }
        @Override
        public void updateUser(UpdateUserRequestDTO request) {

            User user = getLoggedInUser();

            if (request.getFirstName() != null &&
                    !request.getFirstName().isBlank()) {

                user.setFirstName(request.getFirstName());
            }

            if (request.getLastName() != null &&
                    !request.getLastName().isBlank()) {

                user.setLastName(request.getLastName());
            }

            if (request.getEmail() != null &&
                    !request.getEmail().isBlank()) {

                if (!user.getEmail().equals(request.getEmail())
                        && userRepository.existsByEmail(request.getEmail())) {

                    throw new RuntimeException("Email already exists");
                }

                user.setEmail(request.getEmail());
            }
            if (request.getPhoneNumber() != null &&
                    !request.getPhoneNumber().isBlank()) {

                user.setPhoneNumber(request.getPhoneNumber());
            }
            userRepository.save(user);
        }

        @Override
        public void forgotPassword(ForgotPasswordRequestDTO dto) {

            User user = userRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() ->
                            new RuntimeException("User not found with email: " + dto.getEmail())
                    );

            String otp = String.format(
                    "%06d",
                    new Random().nextInt(1000000)
            );

            String redisKey = "password-reset:" + user.getEmail();

            redisTemplate.opsForValue().set(
                    redisKey,
                    otp,
                    5,
                    TimeUnit.MINUTES
            );

            emailService.sendOtpEmail(
                    user.getEmail(),
                    otp
            );
        }
    @Override
    public void resetPassword(ResetPasswordRequestDTO dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }
        String redisKey = "password-reset:" + dto.getEmail();

        Object storedOtp = redisTemplate.opsForValue().get(redisKey);

        if (storedOtp == null) {
            throw new RuntimeException("OTP expired or not found");
        }

        if (!storedOtp.toString().equals(dto.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found with email: " + dto.getEmail())
                );

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        userRepository.save(user);

        // OTP can be used only once
        redisTemplate.delete(redisKey);
    }
    
        private User getLoggedInUser() {

            Authentication authentication = SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            String email = authentication.getName();

            return userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "User not found"
                            ));
        }
    }
