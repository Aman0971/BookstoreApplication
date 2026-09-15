package org.bookstorebackend.dto.response;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDTO {
    private Long productId;
    private String bookName;
    private String author;
    private Integer quantity;
    private Double price;
    private Double totalPrice;
    private String bookImage;
}

