package com.bage.ai.pipeline.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name", nullable = false, length = 100)
    private String projectName;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "local_path", length = 500)
    private String localPath;

    @Column(name = "project_type", length = 50)
    private String projectType;

    @Column(name = "project_source", length = 20)
    @Builder.Default
    private String projectSource = "LOCAL";

    @Column(name = "repo_url", length = 500)
    private String repoUrl;

    @Column(name = "github_token", length = 200)
    private String githubToken;

    @Column(name = "branch", length = 100)
    @Builder.Default
    private String branch = "main";

    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "active";

    @Column(name = "pipeline_count")
    @Builder.Default
    private Integer pipelineCount = 0;

    @Column(name = "success_rate")
    @Builder.Default
    private Double successRate = 0.0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
