package com.bage.ai.pipeline.api.dto.workflow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalSignal {
    private String runId;
    private String approvedBy;
    private String comment;
}