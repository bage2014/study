package com.bage.study.ai.best.practice.dev.flow.repository;

import com.bage.study.ai.best.practice.dev.flow.entity.Multimedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultimediaRepository extends JpaRepository<Multimedia, Long> {

    List<Multimedia> findByFamilyId(Long familyId);

    List<Multimedia> findByMemberId(Long memberId);

    List<Multimedia> findByType(String type);
}