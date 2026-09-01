package org.bookstorebackend.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackResponseDTO {
    private Long id;

    private Long productId;

    private String bookName;

    private String userName;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;
}

