package org.bookstorebackend.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class WishlistItemResponseDTO {
    private Long productId;
    private String bookName;
    private String author;
    private Double price;
}
