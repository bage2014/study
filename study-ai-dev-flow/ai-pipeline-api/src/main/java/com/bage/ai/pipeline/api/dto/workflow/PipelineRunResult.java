package com.bage.ai.pipeline.api.dto.workflow;

import com.bage.ai.pipeline.api.enums.PipelineStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineRunResult {
    private PipelineStatus status;
    private Boolean success;
    private String message;
    private String humanComment;
    private String errorMessage;

    public static PipelineRunResult completed(String message) {
        return PipelineRunResult.builder()
                .status(PipelineStatus.COMPLETED)
                .success(true)
                .message(message)
                .build();
    }

    public static PipelineRunResult failed(String message, String humanComment) {
        return PipelineRunResult.builder()
                .status(PipelineStatus.FAILED)
                .success(false)
                .message(message)
                .humanComment(humanComment)
                .build();
    }
}