package org.bookstorebackend.mapper;
import org.bookstorebackend.dto.request.RegisterRequestDTO;
import org.bookstorebackend.dto.response.RegisterResponseDTO;
import org.bookstorebackend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

        public User toEntity(RegisterRequestDTO dto) {

            return User.builder()
                    .firstName(dto.getFirstName())
                    .lastName(dto.getLastName())
                    .email(dto.getEmail())
                    .password(dto.getPassword())
//                    .phoneNumber(dto.getPhoneNumber())
                    .build();
        }

        public RegisterResponseDTO toRegisterResponse(User user) {

            return RegisterResponseDTO.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    //.phoneNumber(user.getPhoneNumber())
                    .role(user.getRole().name())
                    .build();
        }
}