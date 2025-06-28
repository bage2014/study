package com.example.demo.repository;

import com.example.demo.entity.Trajectory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrajectoryRepository extends JpaRepository<Trajectory, Long> {
}