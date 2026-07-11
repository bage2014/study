package com.bage.ai.pipeline.api.activity;

import com.bage.ai.pipeline.api.service.PipelineService;
import com.bage.ai.pipeline.core.activity.PipelineStatusUpdateActivity;
import com.bage.ai.pipeline.core.dto.activity.PipelineStatusUpdateInput;
import com.bage.ai.pipeline.core.dto.workflow.PipelineRunResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PipelineStatusUpdateActivityImpl implements PipelineStatusUpdateActivity {

    private final PipelineService pipelineService;

    public PipelineStatusUpdateActivityImpl(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    @Override
    public void updateStatus(PipelineStatusUpdateInput input) {
        log.info("Updating pipeline status: {} -> {}, stage: {}",
                input.getRunId(), input.getStatus(), input.getStage());
        pipelineService.updatePipelineStatus(input.getRunId(), input.getStatus(), input.getStage());
    }

    @Override
    public void updateResult(String runId, PipelineRunResult result) {
        log.info("Updating pipeline result: {}, success: {}", runId, result.getSuccess());
        pipelineService.updatePipelineResult(runId, result);
    }
}
