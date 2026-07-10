package com.bage.ai.pipeline.api.service;

import com.bage.ai.pipeline.api.entity.ApprovalEntity;
import com.bage.ai.pipeline.api.entity.PipelineRunEntity;
import com.bage.ai.pipeline.api.repository.ApprovalRepository;
import com.bage.ai.pipeline.api.repository.PipelineRunRepository;
import com.bage.ai.pipeline.core.dto.workflow.PipelineStartInput;
import com.bage.ai.pipeline.core.dto.workflow.PipelineRunResult;
import com.bage.ai.pipeline.core.enums.PipelineStatus;
import com.bage.ai.pipeline.core.workflow.PipelineWorkflow;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PipelineService {

    private final WorkflowClient workflowClient;
    private final PipelineRunRepository pipelineRunRepository;
    private final ApprovalRepository approvalRepository;
    private final ObjectMapper objectMapper;

    public PipelineService(WorkflowClient workflowClient,
                           PipelineRunRepository pipelineRunRepository,
                           ApprovalRepository approvalRepository,
                           ObjectMapper objectMapper) {
        this.workflowClient = workflowClient;
        this.pipelineRunRepository = pipelineRunRepository;
        this.approvalRepository = approvalRepository;
        this.objectMapper = objectMapper;
    }

    public String startPipeline(PipelineStartInput input) {
        String pipelineId = UUID.randomUUID().toString();

        try {
            String inputJson = objectMapper.writeValueAsString(input);
            pipelineRunRepository.save(PipelineRunEntity.builder()
                    .pipelineId(pipelineId)
                    .status(PipelineStatus.RUNNING)
                    .currentStage("INIT")
                    .inputJson(inputJson)
                    .build());

            WorkflowOptions options = WorkflowOptions.newBuilder()
                    .setWorkflowId(pipelineId)
                    .setTaskQueue("pipeline-task-queue")
                    .setWorkflowExecutionTimeout(Duration.ofHours(2))
                    .setWorkflowRunTimeout(Duration.ofHours(2))
                    .build();

            PipelineWorkflow workflow = workflowClient.newWorkflowStub(PipelineWorkflow.class, options);
            workflow.start(input);

            log.info("Pipeline started: {}", pipelineId);
            return pipelineId;
        } catch (Exception e) {
            log.error("Failed to start pipeline: {}", e.getMessage());
            throw new RuntimeException("Failed to start pipeline", e);
        }
    }

    public PipelineRunEntity getPipelineStatus(String pipelineId) {
        return pipelineRunRepository.findByPipelineId(pipelineId);
    }

    public List<PipelineRunEntity> getAllPipelines() {
        return pipelineRunRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<PipelineRunEntity> getPipelinesByStatus(PipelineStatus status) {
        return pipelineRunRepository.findByStatus(status);
    }

    public void updatePipelineStatus(String pipelineId, PipelineStatus status, String stage) {
        PipelineRunEntity entity = pipelineRunRepository.findByPipelineId(pipelineId);
        if (entity != null) {
            entity.setStatus(status);
            entity.setCurrentStage(stage);
            pipelineRunRepository.save(entity);
        }
    }

    public void updatePipelineResult(String pipelineId, PipelineRunResult result) {
        PipelineRunEntity entity = pipelineRunRepository.findByPipelineId(pipelineId);
        if (entity != null) {
            try {
                entity.setResultJson(objectMapper.writeValueAsString(result));
                entity.setStatus(result.getSuccess() != null && result.getSuccess()
                        ? PipelineStatus.COMPLETED : PipelineStatus.FAILED);
                if (result.getSuccess() != null && !result.getSuccess() && result.getErrorMessage() != null) {
                    entity.setErrorMessage(result.getErrorMessage());
                }
                pipelineRunRepository.save(entity);
            } catch (Exception e) {
                log.error("Failed to update pipeline result: {}", e.getMessage());
            }
        }
    }

    public void requestApproval(String pipelineId, String stage) {
        approvalRepository.findByPipelineIdAndStage(pipelineId, stage)
                .orElseGet(() -> approvalRepository.save(ApprovalEntity.builder()
                        .pipelineId(pipelineId)
                        .stage(stage)
                        .approved(null)
                        .build()));
    }

    public ApprovalEntity approve(String pipelineId, String stage, boolean approved,
                                   String reviewer, String comment) {
        return approvalRepository.findByPipelineIdAndStage(pipelineId, stage)
                .map(approval -> {
                    approval.setApproved(approved);
                    approval.setReviewer(reviewer);
                    approval.setComment(comment);
                    approval.setApprovedAt(java.time.LocalDateTime.now());
                    return approvalRepository.save(approval);
                })
                .orElseGet(() -> approvalRepository.save(ApprovalEntity.builder()
                        .pipelineId(pipelineId)
                        .stage(stage)
                        .approved(approved)
                        .reviewer(reviewer)
                        .comment(comment)
                        .approvedAt(java.time.LocalDateTime.now())
                        .build()));
    }

    public ApprovalEntity getApproval(String pipelineId, String stage) {
        return approvalRepository.findByPipelineIdAndStage(pipelineId, stage).orElse(null);
    }

    public void cancelPipeline(String pipelineId) {
        workflowClient.newWorkflowStub(PipelineWorkflow.class, pipelineId).cancel();
        updatePipelineStatus(pipelineId, PipelineStatus.CANCELLED, "CANCELLED");
        log.info("Pipeline cancelled: {}", pipelineId);
    }
}