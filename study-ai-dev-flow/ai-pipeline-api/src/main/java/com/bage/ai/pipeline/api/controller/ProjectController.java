package com.bage.ai.pipeline.api.controller;

import com.bage.ai.pipeline.api.entity.ProjectEntity;
import com.bage.ai.pipeline.api.entity.RequirementEntity;
import com.bage.ai.pipeline.api.entity.PipelineRunEntity;
import com.bage.ai.pipeline.api.repository.ProjectRepository;
import com.bage.ai.pipeline.api.repository.RequirementRepository;
import com.bage.ai.pipeline.api.repository.PipelineRunRepository;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.HttpTransport;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.transport.Transport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public ResponseEntity<Map<String, Object>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ProjectEntity> projectPage;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            projectPage = projectRepository.searchByKeyword(keyword.trim(), pageable);
        } else {
            projectPage = projectRepository.findAll(pageable);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", projectPage.getContent());
        response.put("totalElements", projectPage.getTotalElements());
        response.put("totalPages", projectPage.getTotalPages());
        response.put("currentPage", projectPage.getNumber());
        response.put("pageSize", projectPage.getSize());
        
        return ResponseEntity.ok(response);
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
    public ResponseEntity<?> createProject(@RequestBody Map<String, Object> body) {
        String projectName = (String) body.get("projectName");
        String description = (String) body.get("description");
        String localPath = (String) body.get("localPath");
        String projectType = (String) body.get("projectType");
        String projectSource = (String) body.get("projectSource");
        String repoUrl = (String) body.get("repoUrl");
        String githubToken = (String) body.get("githubToken");
        String branch = (String) body.get("branch");

        if (projectRepository.existsByProjectName(projectName)) {
            return ResponseEntity.badRequest().body(Map.of("error", "项目名称已存在"));
        }

        if ("GITHUB".equals(projectSource)) {
            if (repoUrl == null || repoUrl.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "GitHub项目需要提供仓库地址"));
            }
            String clonePath = cloneGitHubRepo(repoUrl, githubToken, projectName, branch);
            if (clonePath == null) {
                return ResponseEntity.internalServerError().body(Map.of("error", "克隆GitHub仓库失败"));
            }
            localPath = clonePath;
        } else {
            if (localPath == null || localPath.isEmpty()) {
                localPath = "/tmp/" + projectName;
            }
            ensureDirectoryExists(localPath);
        }

        ProjectEntity project = ProjectEntity.builder()
                .projectName(projectName)
                .description(description)
                .localPath(localPath)
                .projectType(projectType)
                .projectSource(projectSource != null ? projectSource : "LOCAL")
                .repoUrl(repoUrl)
                .githubToken(githubToken)
                .branch(branch != null ? branch : "main")
                .status("active")
                .pipelineCount(0)
                .successRate(0.0)
                .build();

        return ResponseEntity.ok(projectRepository.save(project));
    }

    private String cloneGitHubRepo(String repoUrl, String githubToken, String projectName, String branch) {
        try {
            String targetPath = "/tmp/" + projectName;
            File targetDir = new File(targetPath);
            if (targetDir.exists()) {
                deleteDirectory(targetDir);
            }

            CloneCommand cloneCommand = Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(targetDir);

            if (branch != null && !branch.isEmpty()) {
                cloneCommand.setBranch(branch);
            }

            if (githubToken != null && !githubToken.isEmpty()) {
                cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(githubToken, ""));
            }

            try (Git git = cloneCommand.call()) {
                log.info("Successfully cloned repository: {} to {}", repoUrl, targetPath);
            }

            return targetPath;
        } catch (Exception e) {
            log.error("Failed to clone repository: {}", e.getMessage());
            return null;
        }
    }

    private void ensureDirectoryExists(String path) {
        try {
            Path dir = Paths.get(path);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (Exception e) {
            log.error("Failed to create directory: {}", e.getMessage());
        }
    }

    private void deleteDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        dir.delete();
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
                    if (body.containsKey("localPath")) {
                        existing.setLocalPath((String) body.get("localPath"));
                    }
                    if (body.containsKey("projectType")) {
                        existing.setProjectType((String) body.get("projectType"));
                    }
                    if (body.containsKey("projectSource")) {
                        existing.setProjectSource((String) body.get("projectSource"));
                    }
                    if (body.containsKey("repoUrl")) {
                        existing.setRepoUrl((String) body.get("repoUrl"));
                    }
                    if (body.containsKey("githubToken")) {
                        existing.setGithubToken((String) body.get("githubToken"));
                    }
                    if (body.containsKey("branch")) {
                        existing.setBranch((String) body.get("branch"));
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
