package com.bage.ai.pipeline.api.repository;

import com.bage.ai.pipeline.api.entity.RequirementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementRepository extends JpaRepository<RequirementEntity, Long> {
    List<RequirementEntity> findByProjectId(Long projectId);
    List<RequirementEntity> findByProjectIdOrderByCreatedAtDesc(Long projectId);
    List<RequirementEntity> findByProjectIdAndStatus(Long projectId, String status);
    RequirementEntity findByPipelineId(String pipelineId);
}