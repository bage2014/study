package com.bage.demo.controller;

import com.bage.demo.dto.FeedbackRequest;
import com.bage.demo.dto.FeedbackResponse;
import com.bage.demo.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing feedback APIs.
 */
@Slf4j
@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    /**
     * Submit new feedback.
     *
     * @param request the feedback request body
     * @return 201 Created with the persisted feedback
     */
    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        log.info("Received feedback submission from {}", request.getEmail());
        FeedbackResponse response = feedbackService.createFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * List all feedback records.
     *
     * @return 200 OK with the list of feedback
     */
    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> listFeedback() {
        List<FeedbackResponse> feedback = feedbackService.getAllFeedback();
        return ResponseEntity.ok(feedback);
    }
}
