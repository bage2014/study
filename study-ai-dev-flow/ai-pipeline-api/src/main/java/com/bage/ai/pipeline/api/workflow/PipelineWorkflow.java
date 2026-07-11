package com.bage.ai.pipeline.api.workflow;

import com.bage.ai.pipeline.api.dto.workflow.ApprovalSignal;
import com.bage.ai.pipeline.api.dto.workflow.PipelineRunResult;
import com.bage.ai.pipeline.api.dto.workflow.PipelineStartInput;
import com.bage.ai.pipeline.api.dto.workflow.RejectionSignal;
import com.bage.ai.pipeline.api.enums.PipelineStatus;
import com.bage.ai.pipeline.api.enums.StageName;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface PipelineWorkflow {

    @WorkflowMethod
    PipelineRunResult start(PipelineStartInput input);

    @SignalMethod
    void approve(ApprovalSignal signal);

    @SignalMethod
    void reject(RejectionSignal signal);

    @QueryMethod
    PipelineStatus getStatus();

    @QueryMethod
    StageName getCurrentStage();

    @SignalMethod
    void cancel();
}