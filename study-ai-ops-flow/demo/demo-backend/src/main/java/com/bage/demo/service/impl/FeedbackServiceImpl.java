package com.bage.demo.service.impl;

import com.bage.demo.dto.FeedbackRequest;
import com.bage.demo.dto.FeedbackResponse;
import com.bage.demo.entity.Feedback;
import com.bage.demo.exception.ResourceNotFoundException;
import com.bage.demo.repository.FeedbackRepository;
import com.bage.demo.service.FeedbackService;
import java.util.List;
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
        log.debug("Creating feedback from name={}, email={}", request.getName(), request.getEmail());
        Feedback feedback = Feedback.builder()
                .name(request.getName())
                .email(request.getEmail())
                .content(request.getContent())
                .rating(request.getRating())
                .build();
        Feedback saved = feedbackRepository.save(feedback);
        log.info("Created feedback with id={}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getAllFeedbacks() {
        return feedbackRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackResponse getFeedbackById(Long id) {
        return toResponse(getEntity(id));
    }

    @Override
    @Transactional
    public FeedbackResponse updateFeedback(Long id, FeedbackRequest request) {
        Feedback feedback = getEntity(id);
        feedback.setName(request.getName());
        feedback.setEmail(request.getEmail());
        feedback.setContent(request.getContent());
        feedback.setRating(request.getRating());
        Feedback saved = feedbackRepository.save(feedback);
        log.info("Updated feedback with id={}", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feedback not found with id: " + id);
        }
        feedbackRepository.deleteById(id);
        log.info("Deleted feedback with id={}", id);
    }

    private Feedback getEntity(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + id));
    }

    private FeedbackResponse toResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .name(feedback.getName())
                .email(feedback.getEmail())
                .content(feedback.getContent())
                .rating(feedback.getRating())
                .build();
    }
}
