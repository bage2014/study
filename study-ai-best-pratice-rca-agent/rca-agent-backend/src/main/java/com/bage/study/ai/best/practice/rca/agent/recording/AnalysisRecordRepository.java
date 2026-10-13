package com.bage.study.ai.best.practice.rca.agent.recording;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisRecordRepository extends JpaRepository<AnalysisRecordEntity, String> {
}
