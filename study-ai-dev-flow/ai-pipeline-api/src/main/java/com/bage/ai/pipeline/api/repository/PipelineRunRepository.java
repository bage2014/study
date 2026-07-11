package com.bage.ai.pipeline.api.repository;

import com.bage.ai.pipeline.api.entity.PipelineRunEntity;
import com.bage.ai.pipeline.api.enums.PipelineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PipelineRunRepository extends JpaRepository<PipelineRunEntity, Long> {

    PipelineRunEntity findByPipelineId(String pipelineId);

    List<PipelineRunEntity> findByStatus(PipelineStatus status);

    List<PipelineRunEntity> findAllByOrderByCreatedAtDesc();
}