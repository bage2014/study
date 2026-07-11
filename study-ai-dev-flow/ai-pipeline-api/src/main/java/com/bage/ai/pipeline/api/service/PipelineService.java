package com.bage.ai.pipeline.api.service;

import com.bage.ai.pipeline.api.entity.ApprovalEntity;
import com.bage.ai.pipeline.api.entity.PipelineRunEntity;
import com.bage.ai.pipeline.api.entity.PipelineStageEntity;
import com.bage.ai.pipeline.api.dto.workflow.PipelineRunResult;
import com.bage.ai.pipeline.api.enums.PipelineStatus;

import java.util.List;
import java.util.Map;

public interface PipelineService {

    String startPipeline(com.bage.ai.pipeline.api.dto.workflow.PipelineStartInput input);

    PipelineRunEntity getPipelineStatus(String pipelineId);

    List<PipelineRunEntity> getAllPipelines();

    List<PipelineRunEntity> getPipelinesByStatus(PipelineStatus status);

    void updatePipelineStatus(String pipelineId, PipelineStatus status, String stage);

    void updatePipelineResult(String pipelineId, PipelineRunResult result);

    void requestApproval(String pipelineId, String stage);

    ApprovalEntity approve(String pipelineId, String stage, boolean approved,
                           String reviewer, String comment);

    ApprovalEntity getApproval(String pipelineId, String stage);

    void cancelPipeline(String pipelineId);

    void recordStageStart(String pipelineId, String stageName, int orderNum);

    void recordStageEnd(String pipelineId, String stageName, String resultJson, String errorMessage);

    List<PipelineStageEntity> getStages(String pipelineId);

    List<Map<String, Object>> getGeneratedFiles(String pipelineId);

    String getFileContent(String pipelineId, String fileName);
}
