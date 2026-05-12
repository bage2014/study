package com.bage.study.ai.best.practice.dev.flow.repository;

import com.bage.study.ai.best.practice.dev.flow.entity.MemberMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberMilestoneRepository extends JpaRepository<MemberMilestone, Long> {

    List<MemberMilestone> findByMemberId(Long memberId);
}