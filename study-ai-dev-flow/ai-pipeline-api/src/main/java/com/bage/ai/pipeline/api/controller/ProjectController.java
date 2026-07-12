package com.bage.ai.pipeline.api.controller;

import com.bage.ai.pipeline.api.entity.ProjectEntity;
import com.bage.ai.pipeline.api.entity.RequirementEntity;
import com.bage.ai.pipeline.api.entity.PipelineRunEntity;
import com.bage.ai.pipeline.api.repository.ProjectRepository;
import com.bage.ai.pipeline.api.repository.RequirementRepository;
import com.bage.ai.pipeline.api.repository.PipelineRunRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/project")
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final RequirementRepository requirementRepository;
    private final PipelineRunRepository pipelineRunRepository;

    public ProjectController(ProjectRepository projectRepository,
                             RequirementRepository requirementRepository,
                             PipelineRunRepository pipelineRunRepository) {
        this.projectRepository = projectRepository;
        this.requirementRepository = requirementRepository;
        this.pipelineRunRepository = pipelineRunRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProjectEntity>> getAllProjects() {
        return ResponseEntity.ok(projectRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectEntity> getProject(@PathVariable Long id) {
        return projectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<Map<String, Object>> getProjectDetail(@PathVariable Long id) {
        return projectRepository.findById(id)
                .map(project -> {
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("project", project);
                    detail.put("requirements", requirementRepository.findByProjectIdOrderByCreatedAtDesc(id));
                    detail.put("pipelines", pipelineRunRepository.findByProjectId(id));
                    return ResponseEntity.ok(detail);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProjectEntity> createProject(@RequestBody Map<String, Object> body) {
        String projectName = (String) body.get("projectName");
        String description = (String) body.get("description");
        String localPath = (String) body.get("localPath");
        String projectType = (String) body.get("projectType");

        if (projectRepository.existsByProjectName(projectName)) {
            return ResponseEntity.badRequest().build();
        }

        ProjectEntity project = ProjectEntity.builder()
                .projectName(projectName)
                .description(description)
                .localPath(localPath)
                .projectType(projectType)
                .status("active")
                .pipelineCount(0)
                .successRate(0.0)
                .build();

        return ResponseEntity.ok(projectRepository.save(project));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectEntity> updateProject(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        return projectRepository.findById(id)
                .map(existing -> {
                    if (body.containsKey("projectName")) {
                        existing.setProjectName((String) body.get("projectName"));
                    }
                    if (body.containsKey("description")) {
                        existing.setDescription((String) body.get("description"));
                    }
                    if (body.containsKey("status")) {
                        existing.setStatus((String) body.get("status"));
                    }
                    return ResponseEntity.ok(projectRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getProjectStats() {
        long total = projectRepository.count();
        long active = projectRepository.findByStatus("active").size();
        
        return ResponseEntity.ok(Map.of(
                "total", total,
                "active", active,
                "inactive", total - active
        ));
    }
}
