package org.bookstorebackend.service.Impl;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.bookstorebackend.dto.request.FeedbackRequestDTO;
import org.bookstorebackend.dto.response.FeedbackResponseDTO;
import org.bookstorebackend.entity.Feedback;
import org.bookstorebackend.entity.Product;
import org.bookstorebackend.entity.User;
import org.bookstorebackend.exception.ResourceNotFoundException;
import org.bookstorebackend.repository.FeedbackRepository;
import org.bookstorebackend.repository.ProductRepository;
import org.bookstorebackend.repository.UserRepository;
import org.bookstorebackend.service.FeedbackService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class FeedbackServiceImpl implements FeedbackService {

        private final FeedbackRepository feedbackRepository;
        private final ProductRepository productRepository;
        private final UserRepository userRepository;

        @Override
        public FeedbackResponseDTO addFeedback(Long productId, FeedbackRequestDTO request) {

            // Get logged-in user
            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            String email = authentication.getName();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("User not found"));

            // Find product
            Product product = productRepository.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found"));

            // Create feedback
            Feedback feedback = Feedback.builder()
                    .rating(request.getRating())
                    .comment(request.getComment())
                    .user(user)
                    .product(product)
                    .build();

            Feedback savedFeedback = feedbackRepository.save(feedback);

            return mapToResponse(savedFeedback);
        }

        @Override
        public List<FeedbackResponseDTO> getFeedbackByProduct(Long productId) {

            return feedbackRepository.findByProductId(productId)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        private FeedbackResponseDTO mapToResponse(Feedback feedback) {

            return FeedbackResponseDTO.builder()
                    .id(feedback.getId())
                    .productId(feedback.getProduct().getId())
                    .bookName(feedback.getProduct().getBookName())
                    .userName(
                            feedback.getUser().getFirstName()
                                    + " "
                                    + feedback.getUser().getLastName()
                    )
                    .rating(feedback.getRating())
                    .comment(feedback.getComment())
                    .createdAt(feedback.getCreatedAt())
                    .build();
        }
    }
