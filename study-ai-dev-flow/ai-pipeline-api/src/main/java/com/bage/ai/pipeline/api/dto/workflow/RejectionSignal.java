package com.bage.ai.pipeline.api.dto.workflow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RejectionSignal {
    private String runId;
    private String rejectedBy;
    private String reason;
}