package com.bage.ai.pipeline.api.repository;

import com.bage.ai.pipeline.api.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByProjectName(String projectName);

    List<ProjectEntity> findByStatus(String status);

    List<ProjectEntity> findByProjectType(String projectType);

    boolean existsByProjectName(String projectName);
}
