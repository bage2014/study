package com.bage.ai.pipeline.api.controller;

import com.bage.ai.pipeline.api.entity.ApprovalEntity;
import com.bage.ai.pipeline.api.entity.PipelineRunEntity;
import com.bage.ai.pipeline.api.entity.PipelineStageEntity;
import com.bage.ai.pipeline.api.service.PipelineService;
import com.bage.ai.pipeline.api.dto.workflow.PipelineStartInput;
import com.bage.ai.pipeline.api.enums.PipelineStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/api/pipeline")
public class PipelineController {

    private final PipelineService pipelineService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

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
        return content.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(content);
    }

    @GetMapping(value = "/{pipelineId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamPipelineStatus(@PathVariable String pipelineId) {
        SseEmitter emitter = new SseEmitter(300000L);
        executorService.execute(() -> {
            try {
                PipelineRunEntity lastEntity = null;
                while (true) {
                    PipelineRunEntity entity = pipelineService.getPipelineStatus(pipelineId);
                    if (entity != null && !entity.equals(lastEntity)) {
                        emitter.send(SseEmitter.event()
                                .name("status")
                                .data(entity));
                        lastEntity = entity;
                        if (entity.getStatus() == PipelineStatus.COMPLETED ||
                            entity.getStatus() == PipelineStatus.FAILED ||
                            entity.getStatus() == PipelineStatus.CANCELLED) {
                            break;
                        }
                    }
                    Thread.sleep(2000);
                }
                emitter.complete();
            } catch (IOException | InterruptedException e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }
}