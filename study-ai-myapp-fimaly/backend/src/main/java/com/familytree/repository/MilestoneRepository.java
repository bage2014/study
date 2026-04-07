package com.familytree.repository;

import com.familytree.model.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findByMemberId(Long memberId);
    List<Milestone> findByMemberIdAndIsPublicTrue(Long memberId);
}
