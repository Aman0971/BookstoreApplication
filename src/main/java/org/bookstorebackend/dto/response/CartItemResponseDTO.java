package org.bookstorebackend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDTO {
    private Long id;
    private Long productId;
    private String bookName;
    private Integer quantity;
    private String author;
    private Double price;
    private Double totalPrice;
    private String bookImage;
}
