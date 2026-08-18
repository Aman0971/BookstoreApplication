package org.bookstorebackend.service.Impl;
import lombok.RequiredArgsConstructor;
import org.bookstorebackend.dto.request.LoginRequestDTO;
import org.bookstorebackend.dto.request.RegisterRequestDTO;
import org.bookstorebackend.dto.response.LoginResponseDTO;
import org.bookstorebackend.dto.response.RegisterResponseDTO;
import org.bookstorebackend.entity.User;
import org.bookstorebackend.mapper.UserMapper;
import org.bookstorebackend.repository.UserRepository;
import org.bookstorebackend.service.UserService;
import org.bookstorebackend.util.JwtUtil;
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
    }
