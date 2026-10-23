package com.bage.ai.ops.api.repository;

import com.bage.ai.ops.api.entity.CodeReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeReviewRepository extends JpaRepository<CodeReviewEntity, Long> {

    Optional<CodeReviewEntity> findByPipelineId(String pipelineId);

    List<CodeReviewEntity> findByHasCriticalIssues(Boolean hasCriticalIssues);
}