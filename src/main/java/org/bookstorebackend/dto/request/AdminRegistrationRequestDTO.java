package org.bookstorebackend.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRegistrationRequestDTO {

        @NotBlank
        private String firstName;

        @NotBlank
        private String lastName;

        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;
}
