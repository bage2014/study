package com.bage.ai.pipeline.api.repository;

import com.bage.ai.pipeline.api.entity.ApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Long> {

    Optional<ApprovalEntity> findByPipelineIdAndStage(String pipelineId, String stage);

    List<ApprovalEntity> findByPipelineId(String pipelineId);

    boolean existsByPipelineIdAndStageAndApprovedIsNotNull(String pipelineId, String stage);
}