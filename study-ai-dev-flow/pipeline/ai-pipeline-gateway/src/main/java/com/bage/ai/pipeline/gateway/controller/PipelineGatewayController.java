package com.bage.ai.pipeline.gateway.controller;

import com.bage.ai.pipeline.core.dto.workflow.PipelineStartInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/pipeline")
@CrossOrigin(origins = "*")
public class PipelineGatewayController {

    private final WebClient webClient;

    public PipelineGatewayController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPipelines() {
        try {
            List<Map<String, Object>> result = webClient.get()
                    .uri("/api/pipeline")
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get pipelines: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping("/{pipelineId}")
    public ResponseEntity<Map<String, Object>> getPipeline(@PathVariable String pipelineId) {
        try {
            Map<String, Object> result = webClient.get()
                    .uri("/api/pipeline/{pipelineId}", pipelineId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (result == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get pipeline: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Map<String, Object>>> getPipelinesByStatus(@PathVariable String status) {
        try {
            List<Map<String, Object>> result = webClient.get()
                    .uri("/api/pipeline/status/{status}", status)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get pipelines by status: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startPipeline(@RequestBody Map<String, Object> body) {
        try {
            PipelineStartInput input = new PipelineStartInput();
            input.setRequirementMd((String) body.get("requirementMd"));
            input.setProjectLocalPath((String) body.get("projectLocalPath"));

            Object projectIdObj = body.get("projectId");
            if (projectIdObj != null) {
                if (projectIdObj instanceof Number) {
                    input.setProjectId(((Number) projectIdObj).longValue());
                } else {
                    input.setProjectId(Long.parseLong(projectIdObj.toString()));
                }
            }

            // 透传可选参数：审批配置、项目类型、构建工具、测试自修复
            Object autoApproveObj = body.get("autoApproveMap");
            if (autoApproveObj instanceof Map<?, ?> rawMap) {
                Map<com.bage.ai.pipeline.core.enums.StageName, Boolean> approveMap = new HashMap<>();
                rawMap.forEach((k, v) -> {
                    try {
                        approveMap.put(com.bage.ai.pipeline.core.enums.StageName.valueOf(String.valueOf(k)),
                                Boolean.TRUE.equals(v) || "true".equalsIgnoreCase(String.valueOf(v)));
                    } catch (IllegalArgumentException e) {
                        log.warn("Unknown stage name in autoApproveMap: {}", k);
                    }
                });
                input.setAutoApproveMap(approveMap);
            }
            Object projectTypeObj = body.get("projectType");
            if (projectTypeObj != null && !projectTypeObj.toString().isBlank()) {
                try {
                    input.setProjectType(com.bage.ai.pipeline.core.enums.ProjectType.valueOf(projectTypeObj.toString()));
                } catch (IllegalArgumentException e) {
                    log.warn("Unknown projectType: {}", projectTypeObj);
                }
            }
            Object buildToolObj = body.get("buildTool");
            if (buildToolObj != null && !buildToolObj.toString().isBlank()) {
                try {
                    input.setBuildTool(com.bage.ai.pipeline.core.enums.BuildTool.valueOf(buildToolObj.toString()));
                } catch (IllegalArgumentException e) {
                    log.warn("Unknown buildTool: {}", buildToolObj);
                }
            }
            Object frontendPathObj = body.get("frontendLocalPath");
            if (frontendPathObj != null && !frontendPathObj.toString().isBlank()) {
                input.setFrontendLocalPath(frontendPathObj.toString());
            }
            Object autoFixObj = body.get("autoFixOnTestFail");
            if (autoFixObj != null) {
                input.setAutoFixOnTestFail(Boolean.TRUE.equals(autoFixObj)
                        || "true".equalsIgnoreCase(String.valueOf(autoFixObj)));
            }
            Object maxRetriesObj = body.get("maxTestFixRetries");
            if (maxRetriesObj instanceof Number) {
                input.setMaxTestFixRetries(((Number) maxRetriesObj).intValue());
            }
            Object sandboxObj = body.get("sandboxEnabled");
            if (sandboxObj != null) {
                input.setSandboxEnabled(Boolean.TRUE.equals(sandboxObj)
                        || "true".equalsIgnoreCase(String.valueOf(sandboxObj)));
            }

            Map<String, Object> result = webClient.post()
                    .uri("/api/pipeline/start")
                    .bodyValue(input)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (result == null) {
                return ResponseEntity.internalServerError().body(Map.of("error", "Failed to start pipeline"));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to start pipeline: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{pipelineId}/stages")
    public ResponseEntity<List<Map<String, Object>>> getStages(@PathVariable String pipelineId) {
        try {
            List<Map<String, Object>> result = webClient.get()
                    .uri("/api/pipeline/{pipelineId}/stages", pipelineId)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get stages: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Map<String, Object>>> getPipelinesByProject(@PathVariable Long projectId) {
        try {
            List<Map<String, Object>> result = webClient.get()
                    .uri("/api/pipeline/project/{projectId}", projectId)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get pipelines by project: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping("/{pipelineId}/files")
    public ResponseEntity<List<Map<String, Object>>> getGeneratedFiles(@PathVariable String pipelineId) {
        try {
            List<Map<String, Object>> result = webClient.get()
                    .uri("/api/pipeline/{pipelineId}/files", pipelineId)
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get generated files: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping("/{pipelineId}/files/{fileName}")
    public ResponseEntity<String> getFileContent(@PathVariable String pipelineId, @PathVariable String fileName) {
        try {
            String content = webClient.get()
                    .uri("/api/pipeline/{pipelineId}/files/{fileName}", pipelineId, fileName)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            if (content == null || content.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            log.error("Failed to get file content: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @PostMapping("/{pipelineId}/cancel")
    public ResponseEntity<Void> cancelPipeline(@PathVariable String pipelineId) {
        try {
            webClient.post()
                    .uri("/api/pipeline/{pipelineId}/cancel", pipelineId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed to cancel pipeline: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @PostMapping("/{pipelineId}/approval/{stage}")
    public ResponseEntity<Map<String, Object>> approve(
            @PathVariable String pipelineId,
            @PathVariable String stage,
            @RequestBody Map<String, Object> body) {
        try {
            Map<String, Object> result = webClient.post()
                    .uri("/api/pipeline/{pipelineId}/approval/{stage}", pipelineId, stage)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (result == null) {
                return ResponseEntity.internalServerError().body(Map.of("error", "Failed to approve"));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to approve pipeline: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping("/{pipelineId}/approval/{stage}")
    public ResponseEntity<Map<String, Object>> getApproval(
            @PathVariable String pipelineId,
            @PathVariable String stage) {
        try {
            Map<String, Object> result = webClient.get()
                    .uri("/api/pipeline/{pipelineId}/approval/{stage}", pipelineId, stage)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (result == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to get approval: {}", e.getMessage());
            return ResponseEntity.status(503).build();
        }
    }
}
