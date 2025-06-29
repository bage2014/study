package com.bage.my.app.end.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bage.my.app.end.point.entity.Trajectory;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface TrajectoryRepository extends JpaRepository<Trajectory, Long> {
    Page<Trajectory> findByTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Trajectory> findByTimeAfter(LocalDateTime start, Pageable pageable);
    Page<Trajectory> findByTimeBefore(LocalDateTime end, Pageable pageable);
}