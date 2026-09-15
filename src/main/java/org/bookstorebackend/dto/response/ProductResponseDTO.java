package org.bookstorebackend.dto.response;

import lombok.*;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class ProductResponseDTO {

        private Long id;
        private String bookName;
        private String author;
        private String description;
        private Double price;
        private Integer quantity;
        private String bookImage;
    }
