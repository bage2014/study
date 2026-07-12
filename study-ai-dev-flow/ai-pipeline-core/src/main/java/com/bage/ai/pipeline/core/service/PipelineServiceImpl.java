package com.bage.ai.pipeline.core.service;

import com.bage.ai.pipeline.api.dto.workflow.PipelineRunResult;
import com.bage.ai.pipeline.api.dto.workflow.PipelineStartInput;
import com.bage.ai.pipeline.api.entity.ApprovalEntity;
import com.bage.ai.pipeline.api.entity.PipelineRunEntity;
import com.bage.ai.pipeline.api.entity.PipelineStageEntity;
import com.bage.ai.pipeline.api.enums.PipelineStatus;
import com.bage.ai.pipeline.api.enums.StageName;
import com.bage.ai.pipeline.api.repository.ApprovalRepository;
import com.bage.ai.pipeline.api.repository.PipelineRunRepository;
import com.bage.ai.pipeline.api.repository.PipelineStageRepository;
import com.bage.ai.pipeline.api.service.PipelineService;
import com.bage.ai.pipeline.api.workflow.PipelineWorkflow;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class PipelineServiceImpl implements PipelineService {

    private final WorkflowClient workflowClient;
    private final PipelineRunRepository pipelineRunRepository;
    private final ApprovalRepository approvalRepository;
    private final PipelineStageRepository pipelineStageRepository;
    private final ObjectMapper objectMapper;

    public PipelineServiceImpl(WorkflowClient workflowClient,
                               PipelineRunRepository pipelineRunRepository,
                               ApprovalRepository approvalRepository,
                               PipelineStageRepository pipelineStageRepository,
                               ObjectMapper objectMapper) {
        this.workflowClient = workflowClient;
        this.pipelineRunRepository = pipelineRunRepository;
        this.approvalRepository = approvalRepository;
        this.pipelineStageRepository = pipelineStageRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public String startPipeline(PipelineStartInput input) {
        String pipelineId = UUID.randomUUID().toString();

        try {
            input.setRunId(pipelineId);
            if (input.getAutoApproveMap() == null) {
                input.setAutoApproveMap(Map.of(
                        StageName.REQUIREMENT_ANALYSIS, true,
                        StageName.FEATURE_POINT_SPLIT, true,
                        StageName.TASK_SPLIT, true,
                        StageName.CODE_GEN, true,
                        StageName.TEST_GEN, true,
                        StageName.CODE_REVIEW, true,
                        StageName.PR_CREATION, true,
                        StageName.TEST_EXEC, true,
                        StageName.BUILD, true,
                        StageName.DEPLOY, true
                ));
            }

            String inputJson = objectMapper.writeValueAsString(input);
            pipelineRunRepository.save(PipelineRunEntity.builder()
                    .pipelineId(pipelineId)
                    .projectId(input.getProjectId())
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

    @Override
    public PipelineRunEntity getPipelineStatus(String pipelineId) {
        return pipelineRunRepository.findByPipelineId(pipelineId);
    }

    @Override
    public List<PipelineRunEntity> getAllPipelines() {
        return pipelineRunRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<PipelineRunEntity> getPipelinesByStatus(PipelineStatus status) {
        return pipelineRunRepository.findByStatus(status);
    }

    @Override
    public List<PipelineRunEntity> getPipelinesByProject(Long projectId) {
        return pipelineRunRepository.findByProjectIdOrderByCreatedAtDesc(projectId);
    }

    @Override
    public void updatePipelineStatus(String pipelineId, PipelineStatus status, String stage) {
        PipelineRunEntity entity = pipelineRunRepository.findByPipelineId(pipelineId);
        if (entity != null) {
            entity.setStatus(status);
            entity.setCurrentStage(stage);
            pipelineRunRepository.save(entity);
        }
    }

    @Override
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

    @Override
    public void requestApproval(String pipelineId, String stage) {
        approvalRepository.findByPipelineIdAndStage(pipelineId, stage)
                .orElseGet(() -> approvalRepository.save(ApprovalEntity.builder()
                        .pipelineId(pipelineId)
                        .stage(stage)
                        .approved(null)
                        .build()));
    }

    @Override
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

    @Override
    public ApprovalEntity getApproval(String pipelineId, String stage) {
        return approvalRepository.findByPipelineIdAndStage(pipelineId, stage).orElse(null);
    }

    @Override
    public void cancelPipeline(String pipelineId) {
        workflowClient.newWorkflowStub(PipelineWorkflow.class, pipelineId).cancel();
        updatePipelineStatus(pipelineId, PipelineStatus.CANCELLED, "CANCELLED");
        log.info("Pipeline cancelled: {}", pipelineId);
    }

    @Override
    public void recordStageStart(String pipelineId, String stageName, int orderNum) {
        pipelineStageRepository.save(PipelineStageEntity.builder()
                .pipelineId(pipelineId)
                .stageName(stageName)
                .status("RUNNING")
                .startTime(LocalDateTime.now())
                .orderNum(orderNum)
                .build());
    }

    @Override
    public void recordStageEnd(String pipelineId, String stageName, String resultJson, String errorMessage) {
        pipelineStageRepository.findByPipelineIdAndStageName(pipelineId, stageName)
                .ifPresent(stage -> {
                    stage.setStatus(errorMessage != null ? "FAILED" : "COMPLETED");
                    stage.setEndTime(LocalDateTime.now());
                    stage.setDurationMs(java.time.Duration.between(stage.getStartTime(), LocalDateTime.now()).toMillis());
                    stage.setResultJson(resultJson);
                    stage.setErrorMessage(errorMessage);
                    pipelineStageRepository.save(stage);
                });
    }

    @Override
    public List<PipelineStageEntity> getStages(String pipelineId) {
        return pipelineStageRepository.findByPipelineIdOrderByOrderNumAsc(pipelineId);
    }

    @Override
    public List<Map<String, Object>> getGeneratedFiles(String pipelineId) {
        PipelineRunEntity entity = getPipelineStatus(pipelineId);
        if (entity == null || entity.getInputJson() == null) {
            return Collections.emptyList();
        }

        try {
            PipelineStartInput input = objectMapper.readValue(entity.getInputJson(), PipelineStartInput.class);
            String projectPath = input.getProjectLocalPath();
            if (projectPath == null || projectPath.isBlank()) {
                return Collections.emptyList();
            }

            Path srcPath = Paths.get(projectPath, "src", "main", "java");
            if (!Files.exists(srcPath)) {
                return Collections.emptyList();
            }

            List<Map<String, Object>> files = new ArrayList<>();
            try (Stream<Path> walk = Files.walk(srcPath)) {
                walk.filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".java"))
                        .forEach(p -> {
                            String relativePath = srcPath.relativize(p).toString();
                            files.add(Map.of(
                                    "name", p.getFileName().toString(),
                                    "path", relativePath,
                                    "fullPath", p.toString()
                            ));
                        });
            }
            return files;
        } catch (Exception e) {
            log.error("Failed to get generated files: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public String getFileContent(String pipelineId, String fileName) {
        PipelineRunEntity entity = getPipelineStatus(pipelineId);
        if (entity == null || entity.getInputJson() == null) {
            return "";
        }

        try {
            PipelineStartInput input = objectMapper.readValue(entity.getInputJson(), PipelineStartInput.class);
            String projectPath = input.getProjectLocalPath();
            if (projectPath == null || projectPath.isBlank()) {
                return "";
            }

            Path srcPath = Paths.get(projectPath, "src", "main", "java");
            Path targetFile = null;

            try (Stream<Path> walk = Files.walk(srcPath)) {
                targetFile = walk.filter(Files::isRegularFile)
                        .filter(p -> p.getFileName().toString().equals(fileName))
                        .findFirst()
                        .orElse(null);
            }

            if (targetFile != null && Files.exists(targetFile)) {
                return Files.readString(targetFile, StandardCharsets.UTF_8);
            }
            return "";
        } catch (Exception e) {
            log.error("Failed to get file content: {}", e.getMessage());
            return "";
        }
    }
}
