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
@Table(name = "code_review")
public class CodeReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pipeline_id", nullable = false)
    private String pipelineId;

    @Column(name = "has_critical_issues", nullable = false)
    private Boolean hasCriticalIssues;

    @Column(name = "issues", columnDefinition = "TEXT")
    private String issues;

    @Column(name = "review_result", columnDefinition = "TEXT")
    private String reviewResult;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}