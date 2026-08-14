package org.bookstorebackend.dto.response;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponseDTO {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
//        private String phoneNumber;
        private String role;
}

