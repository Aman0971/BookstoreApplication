package org.bookstorebackend.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
        @Pattern(
                regexp = "^[0-9]{10}$",
                message = "Phone number must contain 10 digits"
        )
        private String phoneNumber;

        @NotBlank
        private String password;
}
