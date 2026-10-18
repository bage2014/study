package com.bage.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for creating a feedback.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackRequest {

    private String name;
    private String email;
    private String content;
}
