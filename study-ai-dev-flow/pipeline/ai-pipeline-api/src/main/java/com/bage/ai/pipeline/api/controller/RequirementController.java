package com.bage.ai.pipeline.api.controller;

import com.bage.ai.pipeline.api.entity.RequirementEntity;
import com.bage.ai.pipeline.api.repository.RequirementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/requirement")
@CrossOrigin(origins = "*")
public class RequirementController {

    private final RequirementRepository requirementRepository;

    public RequirementController(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<RequirementEntity>> getRequirementsByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(requirementRepository.findByProjectIdOrderByCreatedAtDesc(projectId));
    }

    @GetMapping("/project/{projectId}/stats")
    public ResponseEntity<Map<String, Object>> getProjectRequirementStats(@PathVariable Long projectId) {
        List<RequirementEntity> all = requirementRepository.findByProjectId(projectId);
        long completed = all.stream().filter(r -> "completed".equals(r.getStatus())).count();
        long inProgress = all.stream().filter(r -> "in_progress".equals(r.getStatus())).count();
        long pending = all.stream().filter(r -> "pending".equals(r.getStatus())).count();

        return ResponseEntity.ok(Map.of(
                "total", all.size(),
                "completed", completed,
                "inProgress", inProgress,
                "pending", pending
        ));
    }

    @GetMapping("/project/{projectId}/{status}")
    public ResponseEntity<List<RequirementEntity>> getRequirementsByProjectAndStatus(
            @PathVariable Long projectId,
            @PathVariable String status) {
        return ResponseEntity.ok(requirementRepository.findByProjectIdAndStatus(projectId, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequirementEntity> getRequirement(@PathVariable Long id) {
        return requirementRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pipeline/{pipelineId}")
    public ResponseEntity<RequirementEntity> getRequirementByPipelineId(@PathVariable String pipelineId) {
        RequirementEntity requirement = requirementRepository.findByPipelineId(pipelineId);
        return requirement != null ? ResponseEntity.ok(requirement) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<RequirementEntity> createRequirement(@RequestBody Map<String, Object> body) {
        Object projectIdObj = body.get("projectId");
        Long projectId;
        if (projectIdObj instanceof Number) {
            projectId = ((Number) projectIdObj).longValue();
        } else if (projectIdObj instanceof String) {
            projectId = Long.parseLong((String) projectIdObj);
        } else {
            return ResponseEntity.badRequest().build();
        }
        
        String title = (String) body.get("title");
        String description = (String) body.get("description");

        RequirementEntity requirement = RequirementEntity.builder()
                .projectId(projectId)
                .title(title)
                .description(description)
                .status("pending")
                .build();

        return ResponseEntity.ok(requirementRepository.save(requirement));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequirementEntity> updateRequirement(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        return requirementRepository.findById(id)
                .map(existing -> {
                    if (body.containsKey("title")) {
                        existing.setTitle((String) body.get("title"));
                    }
                    if (body.containsKey("description")) {
                        existing.setDescription((String) body.get("description"));
                    }
                    if (body.containsKey("status")) {
                        existing.setStatus((String) body.get("status"));
                    }
                    if (body.containsKey("stage")) {
                        existing.setStage((String) body.get("stage"));
                    }
                    if (body.containsKey("pipelineId")) {
                        existing.setPipelineId((String) body.get("pipelineId"));
                    }
                    return ResponseEntity.ok(requirementRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequirement(@PathVariable Long id) {
        if (requirementRepository.existsById(id)) {
            requirementRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}