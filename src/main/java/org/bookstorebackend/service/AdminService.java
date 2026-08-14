package org.bookstorebackend.service;
import org.bookstorebackend.dto.request.AdminLoginRequestDTO;
import org.bookstorebackend.dto.request.AdminRegistrationRequestDTO;
import org.bookstorebackend.dto.response.LoginResponseDTO;
import org.bookstorebackend.dto.response.RegisterResponseDTO;

    public interface AdminService {

        RegisterResponseDTO registerAdmin(AdminRegistrationRequestDTO request);
        LoginResponseDTO loginAdmin(AdminLoginRequestDTO request);
    }
