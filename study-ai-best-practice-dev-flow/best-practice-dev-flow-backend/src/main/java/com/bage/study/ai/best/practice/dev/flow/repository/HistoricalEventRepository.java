package com.bage.study.ai.best.practice.dev.flow.repository;

import com.bage.study.ai.best.practice.dev.flow.entity.HistoricalEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricalEventRepository extends JpaRepository<HistoricalEvent, Long> {

    List<HistoricalEvent> findByFamilyId(Long familyId);
}