package com.bage.study.ai.best.practice.rca.agent.recording;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecordingRepository extends JpaRepository<RecordingEntity, String> {

    Optional<RecordingEntity> findByAnalysisId(String analysisId);
}
