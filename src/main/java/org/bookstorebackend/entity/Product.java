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
    @Table(name = "products")
    public class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String bookName;

        @Column(nullable = false)
        private String author;

        @Column(nullable = false)
        private String description;

        @Column(nullable = false)
        private Double price;

        @Column(nullable = false)
        private Integer quantity;

        @Column(columnDefinition = "TEXT")
        private String bookImage;

        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        @PrePersist
        protected void onCreate() {
            createdAt = LocalDateTime.now();
            updatedAt = LocalDateTime.now();
        }

        @PreUpdate
        protected void onUpdate() {
            updatedAt = LocalDateTime.now();
        }
    }
