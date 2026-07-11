package com.bage.ai.pipeline.api.dto.activity;

import com.bage.ai.pipeline.api.enums.PipelineStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineStatusUpdateInput {

    private String runId;
    private PipelineStatus status;
    private String stage;
}
