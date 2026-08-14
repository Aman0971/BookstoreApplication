package org.bookstorebackend.dto.request;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
    public class ProductRequestDTO {

        @NotBlank
        private String bookName;

        @NotBlank
        private String author;

        @NotBlank
        private String description;

        @NotNull
        @Positive
        private Double price;

        @NotNull
        @PositiveOrZero
        private Integer quantity;

//        private String bookImage;
    }
