package com.bage.demo.dto;

import com.bage.demo.entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO representing a feedback record.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResponse {

    private Long id;
    private String name;
    private String email;
    private String content;
    private LocalDateTime createdAt;

    /**
     * Map a Feedback entity to a FeedbackResponse DTO.
     *
     * @param feedback the entity
     * @return the response DTO
     */
    public static FeedbackResponse fromEntity(Feedback feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .name(feedback.getName())
                .email(feedback.getEmail())
                .content(feedback.getContent())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}
