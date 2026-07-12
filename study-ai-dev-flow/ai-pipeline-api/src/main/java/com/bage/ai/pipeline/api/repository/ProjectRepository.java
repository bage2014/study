package com.bage.ai.pipeline.api.repository;

import com.bage.ai.pipeline.api.entity.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByProjectName(String projectName);

    List<ProjectEntity> findByStatus(String status);

    List<ProjectEntity> findByProjectType(String projectType);

    boolean existsByProjectName(String projectName);

    Page<ProjectEntity> findAll(Pageable pageable);

    @Query("SELECT p FROM ProjectEntity p WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(p.projectName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<ProjectEntity> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
