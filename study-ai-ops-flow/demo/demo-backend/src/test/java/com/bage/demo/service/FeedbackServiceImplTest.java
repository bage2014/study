package com.bage.demo.service;

import com.bage.demo.dto.FeedbackRequest;
import com.bage.demo.dto.FeedbackResponse;
import com.bage.demo.entity.Feedback;
import com.bage.demo.repository.FeedbackRepository;
import com.bage.demo.service.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceImplTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    private Feedback feedback;
    private FeedbackRequest feedbackRequest;

    @BeforeEach
    void setUp() {
        feedback = Feedback.builder()
                .id(1L)
                .content("Great service")
                .rating(5)
                .email("test@example.com")
                .createdAt(LocalDateTime.now())
                .build();

        feedbackRequest = FeedbackRequest.builder()
                .content("Great service")
                .rating(5)
                .email("test@example.com")
                .build();
    }

    @Test
    void createFeedback_shouldReturnSavedFeedback() {
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        FeedbackResponse response = feedbackService.createFeedback(feedbackRequest);

        assertNotNull(response);
        assertEquals(feedback.getId(), response.getId());
        assertEquals(feedback.getContent(), response.getContent());
        assertEquals(feedback.getRating(), response.getRating());
        assertEquals(feedback.getEmail(), response.getEmail());
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void getAllFeedbacks_shouldReturnListOfFeedbacks() {
        Feedback feedback2 = Feedback.builder()
                .id(2L)
                .content("Bad service")
                .rating(1)
                .email("bad@example.com")
                .createdAt(LocalDateTime.now())
                .build();
        when(feedbackRepository.findAll()).thenReturn(Arrays.asList(feedback, feedback2));

        List<FeedbackResponse> responses = feedbackService.getAllFeedbacks();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(feedback.getId(), responses.get(0).getId());
        assertEquals(feedback2.getId(), responses.get(1).getId());
        verify(feedbackRepository, times(1)).findAll();
    }

    @Test
    void getFeedbackById_shouldReturnFeedback_whenExists() {
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));

        FeedbackResponse response = feedbackService.getFeedbackById(1L);

        assertNotNull(response);
        assertEquals(feedback.getId(), response.getId());
        verify(feedbackRepository, times(1)).findById(1L);
    }

    @Test
    void getFeedbackById_shouldThrowException_whenNotFound() {
        when(feedbackRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> feedbackService.getFeedbackById(99L));
        verify(feedbackRepository, times(1)).findById(99L);
    }

    @Test
    void updateFeedback_shouldReturnUpdatedFeedback() {
        FeedbackRequest updateRequest = FeedbackRequest.builder()
                .content("Updated content")
                .rating(4)
                .email("updated@example.com")
                .build();
        Feedback updatedFeedback = Feedback.builder()
                .id(1L)
                .content("Updated content")
                .rating(4)
                .email("updated@example.com")
                .createdAt(feedback.getCreatedAt())
                .build();

        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(updatedFeedback);

        FeedbackResponse response = feedbackService.updateFeedback(1L, updateRequest);

        assertNotNull(response);
        assertEquals("Updated content", response.getContent());
        assertEquals(4, response.getRating());
        assertEquals("updated@example.com", response.getEmail());
        verify(feedbackRepository, times(1)).findById(1L);
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void updateFeedback_shouldThrowException_whenNotFound() {
        when(feedbackRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> feedbackService.updateFeedback(99L, feedbackRequest));
        verify(feedbackRepository, times(1)).findById(99L);
        verify(feedbackRepository, never()).save(any(Feedback.class));
    }

    @Test
    void deleteFeedback_shouldDeleteSuccessfully() {
        when(feedbackRepository.existsById(1L)).thenReturn(true);
        doNothing().when(feedbackRepository).deleteById(1L);

        feedbackService.deleteFeedback(1L);

        verify(feedbackRepository, times(1)).existsById(1L);
        verify(feedbackRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteFeedback_shouldThrowException_whenNotFound() {
        when(feedbackRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> feedbackService.deleteFeedback(99L));
        verify(feedbackRepository, times(1)).existsById(99L);
        verify(feedbackRepository, never()).deleteById(anyLong());
    }
}