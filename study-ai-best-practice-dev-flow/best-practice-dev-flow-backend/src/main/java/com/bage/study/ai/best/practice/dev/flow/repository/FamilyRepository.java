package com.bage.study.ai.best.practice.dev.flow.repository;

import com.bage.study.ai.best.practice.dev.flow.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {

    List<Family> findByCreatorId(Long creatorId);

    List<Family> findByAdministratorId(Long administratorId);

    boolean existsByName(String name);
}