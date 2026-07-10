package com.bage.ai.pipeline.api.controller;

import com.bage.ai.pipeline.api.entity.ApprovalEntity;
import com.bage.ai.pipeline.api.entity.PipelineRunEntity;
import com.bage.ai.pipeline.api.service.PipelineService;
import com.bage.ai.pipeline.core.dto.workflow.PipelineStartInput;
import com.bage.ai.pipeline.core.enums.PipelineStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/pipeline")
public class PipelineController {

    private final PipelineService pipelineService;

    public PipelineController(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startPipeline(@RequestBody PipelineStartInput input) {
        log.info("Starting pipeline with requirement: {}", input.getRequirementMd() != null ? "provided" : "null");
        String pipelineId = pipelineService.startPipeline(input);
        return ResponseEntity.ok(Map.of("pipelineId", pipelineId));
    }

    @GetMapping("/{pipelineId}")
    public ResponseEntity<PipelineRunEntity> getPipeline(@PathVariable String pipelineId) {
        PipelineRunEntity entity = pipelineService.getPipelineStatus(pipelineId);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<PipelineRunEntity>> getAllPipelines() {
        return ResponseEntity.ok(pipelineService.getAllPipelines());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PipelineRunEntity>> getPipelinesByStatus(@PathVariable String status) {
        try {
            PipelineStatus pipelineStatus = PipelineStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(pipelineService.getPipelinesByStatus(pipelineStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{pipelineId}/approval/{stage}")
    public ResponseEntity<ApprovalEntity> approve(
            @PathVariable String pipelineId,
            @PathVariable String stage,
            @RequestBody Map<String, Object> body) {
        boolean approved = Boolean.TRUE.equals(body.get("approved"));
        String reviewer = (String) body.get("reviewer");
        String comment = (String) body.get("comment");
        ApprovalEntity result = pipelineService.approve(pipelineId, stage, approved, reviewer, comment);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{pipelineId}/approval/{stage}")
    public ResponseEntity<ApprovalEntity> getApproval(
            @PathVariable String pipelineId,
            @PathVariable String stage) {
        ApprovalEntity approval = pipelineService.getApproval(pipelineId, stage);
        return approval != null ? ResponseEntity.ok(approval) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{pipelineId}/cancel")
    public ResponseEntity<Void> cancelPipeline(@PathVariable String pipelineId) {
        pipelineService.cancelPipeline(pipelineId);
        return ResponseEntity.ok().build();
    }
}