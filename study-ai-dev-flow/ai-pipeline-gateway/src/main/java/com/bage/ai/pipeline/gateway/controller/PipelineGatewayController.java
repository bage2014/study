package com.bage.ai.pipeline.gateway.controller;

import com.bage.ai.pipeline.api.dto.workflow.PipelineStartInput;
import com.bage.ai.pipeline.api.entity.PipelineRunEntity;
import com.bage.ai.pipeline.api.entity.PipelineStageEntity;
import com.bage.ai.pipeline.api.service.PipelineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/pipeline")
@CrossOrigin(origins = "*")
public class PipelineGatewayController {

    private final PipelineService pipelineService;

    public PipelineGatewayController(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    @GetMapping
    public ResponseEntity<List<PipelineRunEntity>> getAllPipelines() {
        return ResponseEntity.ok(pipelineService.getAllPipelines());
    }

    @GetMapping("/{pipelineId}")
    public ResponseEntity<PipelineRunEntity> getPipeline(@PathVariable String pipelineId) {
        PipelineRunEntity pipeline = pipelineService.getPipelineStatus(pipelineId);
        if (pipeline == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pipeline);
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startPipeline(@RequestBody Map<String, String> body) {
        try {
            PipelineStartInput input = new PipelineStartInput();
            input.setRequirementMd(body.get("requirementMd"));
            input.setProjectLocalPath(body.get("projectLocalPath"));

            String pipelineId = pipelineService.startPipeline(input);
            Map<String, String> result = new HashMap<>();
            result.put("pipelineId", pipelineId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to start pipeline: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{pipelineId}/stages")
    public ResponseEntity<List<PipelineStageEntity>> getStages(@PathVariable String pipelineId) {
        return ResponseEntity.ok(pipelineService.getStages(pipelineId));
    }

    @GetMapping("/{pipelineId}/files")
    public ResponseEntity<List<Map<String, Object>>> getGeneratedFiles(@PathVariable String pipelineId) {
        return ResponseEntity.ok(pipelineService.getGeneratedFiles(pipelineId));
    }

    @GetMapping("/{pipelineId}/files/{fileName}")
    public ResponseEntity<String> getFileContent(@PathVariable String pipelineId, @PathVariable String fileName) {
        String content = pipelineService.getFileContent(pipelineId, fileName);
        if (content.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(content);
    }

    @PostMapping("/{pipelineId}/cancel")
    public ResponseEntity<Void> cancelPipeline(@PathVariable String pipelineId) {
        try {
            pipelineService.cancelPipeline(pipelineId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed to cancel pipeline: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
