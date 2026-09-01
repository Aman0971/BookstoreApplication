package org.bookstorebackend.dto.request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

    @Getter
    @Setter
    public class AddMultipleCartRequestDTO {

        @NotEmpty(message = "Cart items cannot be empty")
        @Valid
        private List<CartItemRequestDTO> items;
    }
