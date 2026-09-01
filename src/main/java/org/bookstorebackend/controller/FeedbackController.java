package org.bookstorebackend.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookstorebackend.dto.request.FeedbackRequestDTO;
import org.bookstorebackend.dto.response.FeedbackResponseDTO;
import org.bookstorebackend.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/api/user")
    @RequiredArgsConstructor
    public class FeedbackController {

        private final FeedbackService feedbackService;

        @PostMapping("/add/feedback/{productId}")
        public ResponseEntity<FeedbackResponseDTO> addFeedback(
                @PathVariable Long productId,
                @Valid @RequestBody FeedbackRequestDTO request) {

            return ResponseEntity.ok(
                    feedbackService.addFeedback(productId, request)
            );
        }

        @GetMapping("/get/feedback/{productId}")
        public ResponseEntity<List<FeedbackResponseDTO>> getFeedback(
                @PathVariable Long productId) {

            return ResponseEntity.ok(
                    feedbackService.getFeedbackByProduct(productId)
            );
        }
    }

