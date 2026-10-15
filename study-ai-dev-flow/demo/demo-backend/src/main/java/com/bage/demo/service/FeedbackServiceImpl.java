package com.bage.demo.service;

import com.bage.demo.dto.FeedbackRequest;
import com.bage.demo.entity.Feedback;
import com.bage.demo.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Default implementation of {@link FeedbackService}.
 *
 * @author bage
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Override
    @Transactional
    public Feedback createFeedback(FeedbackRequest request) {
        log.debug("Creating feedback from name={}, email={}", request.getName(), request.getEmail());

        Feedback feedback = Feedback.builder()
                .name(request.getName())
                .email(request.getEmail())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        Feedback saved = feedbackRepository.save(feedback);
        log.info("Feedback persisted with id={}", saved.getId());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Feedback> getAllFeedback() {
        log.debug("Fetching all feedback records");
        return feedbackRepository.findAllByOrderByCreatedAtDesc();
    }
}
