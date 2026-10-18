package com.bage.demo.service.impl;

import com.bage.demo.dto.FeedbackRequest;
import com.bage.demo.dto.FeedbackResponse;
import com.bage.demo.entity.Feedback;
import com.bage.demo.repository.FeedbackRepository;
import com.bage.demo.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default {@link FeedbackService} implementation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Override
    @Transactional
    public FeedbackResponse createFeedback(FeedbackRequest request) {
        Feedback feedback = Feedback.builder()
                .name(request.getName())
                .email(request.getEmail())
                .content(request.getContent())
                .build();
        Feedback saved = feedbackRepository.save(feedback);
        log.info("Created feedback with id={}", saved.getId());
        return FeedbackResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .content(saved.getContent())
                .build();
    }
}
