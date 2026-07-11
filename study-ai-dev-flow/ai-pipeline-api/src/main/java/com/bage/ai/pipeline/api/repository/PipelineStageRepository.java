package com.bage.ai.pipeline.api.repository;

import com.bage.ai.pipeline.api.entity.PipelineStageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PipelineStageRepository extends JpaRepository<PipelineStageEntity, Long> {

    List<PipelineStageEntity> findByPipelineIdOrderByOrderNumAsc(String pipelineId);

    Optional<PipelineStageEntity> findByPipelineIdAndStageName(String pipelineId, String stageName);
}
