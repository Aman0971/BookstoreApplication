package org.bookstorebackend.service;

import org.bookstorebackend.dto.request.LoginRequestDTO;
import org.bookstorebackend.dto.request.RegisterRequestDTO;
import org.bookstorebackend.dto.response.LoginResponseDTO;
import org.bookstorebackend.dto.response.RegisterResponseDTO;

public interface UserService {
        RegisterResponseDTO register(RegisterRequestDTO request);
        LoginResponseDTO login(LoginRequestDTO request);
}

