package org.bookstorebackend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
    public class FeedbackRequestDTO {

        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating cannot be more than 5")
        private Integer rating;

        @NotBlank(message = "Comment cannot be blank")
        private String comment;
    }
