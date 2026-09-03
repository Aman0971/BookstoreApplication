package org.bookstorebackend.service;

import org.bookstorebackend.dto.request.*;
import org.bookstorebackend.dto.response.LoginResponseDTO;
import org.bookstorebackend.dto.response.RegisterResponseDTO;

public interface UserService {
        RegisterResponseDTO register(RegisterRequestDTO request);
        LoginResponseDTO login(LoginRequestDTO request);
        void updateUser(UpdateUserRequestDTO request);

        void forgotPassword(ForgotPasswordRequestDTO dto);
        void resetPassword(ResetPasswordRequestDTO dto);
}

