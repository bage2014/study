package com.bage.demo.service;

import com.bage.demo.dto.FeedbackRequest;
import com.bage.demo.dto.FeedbackResponse;

/**
 * Service contract for feedback operations.
 */
public interface FeedbackService {

    /**
     * Create a new feedback.
     *
     * @param request the feedback creation request
     * @return the created feedback
     */
    FeedbackResponse createFeedback(FeedbackRequest request);
}
