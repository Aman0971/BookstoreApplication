package org.bookstorebackend.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "users")

public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String firstName;

        @Column(nullable = false)
        private String lastName;

        @Column(nullable = false, unique = true)
        private String email;


//        if i put unique = true in phone number then i can not put same phone number in different emails.
        @Column(nullable = false)
        private String phoneNumber;

        @Column(nullable = false)
        private String password;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Role role;

        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        @PrePersist
        protected void onCreate() {
            createdAt = LocalDateTime.now();
            updatedAt = LocalDateTime.now();

            if (role == null) {
                role = Role.USER;
            }
        }
        @PreUpdate
        protected void onUpdate() {
            updatedAt = LocalDateTime.now();
        }
        public enum Role {
            USER,
            ADMIN
        }
        @Column(unique = true)
        private String verificationToken;

        @Column(nullable = false)
        private boolean verified;
}
