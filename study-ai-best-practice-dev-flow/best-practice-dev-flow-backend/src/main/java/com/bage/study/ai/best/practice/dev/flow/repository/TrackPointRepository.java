package com.bage.study.ai.best.practice.dev.flow.repository;

import com.bage.study.ai.best.practice.dev.flow.entity.TrackPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackPointRepository extends JpaRepository<TrackPoint, Long> {

    List<TrackPoint> findByUserIdOrderByCreatedAtAsc(Long userId);

    void deleteByUserId(Long userId);
}