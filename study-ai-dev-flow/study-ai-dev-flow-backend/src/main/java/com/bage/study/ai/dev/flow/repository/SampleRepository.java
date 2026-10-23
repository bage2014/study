package com.bage.study.ai.dev.flow.repository;

import com.bage.study.ai.dev.flow.entity.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends JpaRepository<SampleEntity, Long> {
}
