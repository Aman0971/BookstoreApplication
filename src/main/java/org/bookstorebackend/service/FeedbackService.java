package org.bookstorebackend.service;
import org.bookstorebackend.dto.request.FeedbackRequestDTO;
import org.bookstorebackend.dto.response.FeedbackResponseDTO;

import java.util.List;

public interface FeedbackService {
    FeedbackResponseDTO addFeedback(Long productId, FeedbackRequestDTO request);
    List<FeedbackResponseDTO> getFeedbackByProduct(Long productId);
}
