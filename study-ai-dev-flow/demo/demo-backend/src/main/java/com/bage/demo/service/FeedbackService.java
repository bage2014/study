package com.bage.demo.service;

import com.bage.demo.dto.FeedbackRequest;
import com.bage.demo.dto.FeedbackResponse;
import com.bage.demo.entity.Feedback;
import com.bage.demo.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for feedback operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    /**
     * Persist a new feedback record.
     *
     * @param request the feedback request
     * @return the persisted feedback as a response DTO
     */
    @Transactional
    public FeedbackResponse createFeedback(FeedbackRequest request) {
        Feedback feedback = Feedback.builder()
                .name(request.getName())
                .email(request.getEmail())
                .content(request.getContent())
                .build();

        Feedback saved = feedbackRepository.save(feedback);
        log.info("Saved feedback with id={}", saved.getId());
        return FeedbackResponse.fromEntity(saved);
    }

    /**
     * Retrieve all feedback records.
     *
     * @return list of feedback responses
     */
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getAllFeedback() {
        return feedbackRepository.findAll()
                .stream()
                .map(FeedbackResponse::fromEntity)
                .toList();
    }
}
