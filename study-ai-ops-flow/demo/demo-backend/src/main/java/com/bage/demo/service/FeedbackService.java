package com.bage.demo.service;

import com.bage.demo.dto.FeedbackRequest;
import com.bage.demo.dto.FeedbackResponse;
import java.util.List;

/**
 * Service contract for feedback operations.
 */
public interface FeedbackService {

    FeedbackResponse createFeedback(FeedbackRequest request);

    List<FeedbackResponse> getAllFeedbacks();

    FeedbackResponse getFeedbackById(Long id);

    FeedbackResponse updateFeedback(Long id, FeedbackRequest request);

    void deleteFeedback(Long id);
}
