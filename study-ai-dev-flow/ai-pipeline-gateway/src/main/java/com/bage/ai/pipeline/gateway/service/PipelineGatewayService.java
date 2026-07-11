package com.bage.ai.pipeline.gateway.service;

import com.bage.ai.pipeline.api.dto.workflow.PipelineStartInput;
import com.bage.ai.pipeline.api.entity.PipelineRunEntity;
import com.bage.ai.pipeline.api.entity.PipelineStageEntity;
import com.bage.ai.pipeline.api.service.PipelineService;
import com.bage.ai.pipeline.api.dto.workflow.PipelineRunResult;
import com.bage.ai.pipeline.api.enums.PipelineStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PipelineGatewayService implements PipelineService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public PipelineGatewayService(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public String startPipeline(PipelineStartInput input) {
        try {
            String response = webClient.post()
                    .uri("/start")
                    .bodyValue(input)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            Map<String, String> result = objectMapper.readValue(response, new TypeReference<Map<String, String>>() {});
            return result.get("pipelineId");
        } catch (Exception e) {
            log.error("Failed to start pipeline: {}", e.getMessage());
            throw new RuntimeException("Failed to start pipeline", e);
        }
    }

    @Override
    public PipelineRunEntity getPipelineStatus(String pipelineId) {
        try {
            String response = webClient.get()
                    .uri("/{pipelineId}", pipelineId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return objectMapper.readValue(response, PipelineRunEntity.class);
        } catch (Exception e) {
            log.error("Failed to get pipeline: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<PipelineRunEntity> getAllPipelines() {
        try {
            String response = webClient.get()
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return objectMapper.readValue(response, new TypeReference<List<PipelineRunEntity>>() {});
        } catch (Exception e) {
            log.error("Failed to get pipelines: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<PipelineRunEntity> getPipelinesByStatus(PipelineStatus status) {
        try {
            String response = webClient.get()
                    .uri("/status/{status}", status.name())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return objectMapper.readValue(response, new TypeReference<List<PipelineRunEntity>>() {});
        } catch (Exception e) {
            log.error("Failed to get pipelines by status: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public void updatePipelineStatus(String pipelineId, PipelineStatus status, String stage) {
        throw new UnsupportedOperationException("Not supported in gateway");
    }

    @Override
    public void updatePipelineResult(String pipelineId, PipelineRunResult result) {
        throw new UnsupportedOperationException("Not supported in gateway");
    }

    @Override
    public void requestApproval(String pipelineId, String stage) {
        throw new UnsupportedOperationException("Not supported in gateway");
    }

    @Override
    public com.bage.ai.pipeline.api.entity.ApprovalEntity approve(String pipelineId, String stage, boolean approved,
                                                                  String reviewer, String comment) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("approved", approved);
            body.put("reviewer", reviewer);
            body.put("comment", comment);

            String response = webClient.post()
                    .uri("/{pipelineId}/approval/{stage}", pipelineId, stage)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return objectMapper.readValue(response, com.bage.ai.pipeline.api.entity.ApprovalEntity.class);
        } catch (Exception e) {
            log.error("Failed to approve: {}", e.getMessage());
            throw new RuntimeException("Failed to approve", e);
        }
    }

    @Override
    public com.bage.ai.pipeline.api.entity.ApprovalEntity getApproval(String pipelineId, String stage) {
        try {
            String response = webClient.get()
                    .uri("/{pipelineId}/approval/{stage}", pipelineId, stage)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return objectMapper.readValue(response, com.bage.ai.pipeline.api.entity.ApprovalEntity.class);
        } catch (Exception e) {
            log.error("Failed to get approval: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void cancelPipeline(String pipelineId) {
        try {
            webClient.post()
                    .uri("/{pipelineId}/cancel", pipelineId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            log.error("Failed to cancel pipeline: {}", e.getMessage());
            throw new RuntimeException("Failed to cancel pipeline", e);
        }
    }

    @Override
    public void recordStageStart(String pipelineId, String stageName, int orderNum) {
        throw new UnsupportedOperationException("Not supported in gateway");
    }

    @Override
    public void recordStageEnd(String pipelineId, String stageName, String resultJson, String errorMessage) {
        throw new UnsupportedOperationException("Not supported in gateway");
    }

    @Override
    public List<PipelineStageEntity> getStages(String pipelineId) {
        try {
            String response = webClient.get()
                    .uri("/{pipelineId}/stages", pipelineId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return objectMapper.readValue(response, new TypeReference<List<PipelineStageEntity>>() {});
        } catch (Exception e) {
            log.error("Failed to get stages: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<Map<String, Object>> getGeneratedFiles(String pipelineId) {
        try {
            String response = webClient.get()
                    .uri("/{pipelineId}/files", pipelineId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return objectMapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            log.error("Failed to get generated files: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public String getFileContent(String pipelineId, String fileName) {
        try {
            return webClient.get()
                    .uri("/{pipelineId}/files/{fileName}", pipelineId, fileName)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Failed to get file content: {}", e.getMessage());
            return "";
        }
    }
}
