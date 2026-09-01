package org.bookstorebackend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
    public class UpdateUserRequestDTO {
        @Email(message = "Invalid email format")
        private String email;
        
        private String firstName;
        private String lastName;
    }

