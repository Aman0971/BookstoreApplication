package org.bookstorebackend.service.Impl;
import lombok.RequiredArgsConstructor;
import org.bookstorebackend.dto.request.LoginRequestDTO;
import org.bookstorebackend.dto.request.RegisterRequestDTO;
import org.bookstorebackend.dto.request.UpdateUserRequestDTO;
import org.bookstorebackend.dto.response.LoginResponseDTO;
import org.bookstorebackend.dto.response.RegisterResponseDTO;
import org.bookstorebackend.entity.User;
import org.bookstorebackend.exception.ResourceNotFoundException;
import org.bookstorebackend.mapper.UserMapper;
import org.bookstorebackend.repository.UserRepository;
import org.bookstorebackend.service.UserService;
import org.bookstorebackend.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;
        private final UserMapper userMapper;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;

        @Override
        public RegisterResponseDTO register(RegisterRequestDTO request) {

            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already registered");
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
            userRepository.save(user);
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
