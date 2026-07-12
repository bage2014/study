package com.bage.ai.pipeline.api.activity;

import com.bage.ai.pipeline.core.dto.activity.PipelineStatusUpdateInput;
import com.bage.ai.pipeline.core.dto.workflow.PipelineRunResult;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface PipelineStatusUpdateActivity {

    @ActivityMethod(name = "UpdatePipelineStatus")
    void updateStatus(PipelineStatusUpdateInput input);

    @ActivityMethod(name = "UpdatePipelineResult")
    void updateResult(String runId, PipelineRunResult result);

    @ActivityMethod(name = "RecordStageStart")
    void recordStageStart(String runId, String stageName, int orderNum);

    @ActivityMethod(name = "RecordStageEnd")
    void recordStageEnd(String runId, String stageName, String resultJson, String errorMessage);
}
