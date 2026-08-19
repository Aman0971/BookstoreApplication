package org.bookstorebackend.dto.response;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long orderId;
    private Long userId;
    private Double totalPrice;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItemResponseDTO> items;
}

