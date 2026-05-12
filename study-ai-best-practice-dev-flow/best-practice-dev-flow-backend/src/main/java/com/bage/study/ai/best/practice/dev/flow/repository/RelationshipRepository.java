package com.bage.study.ai.best.practice.dev.flow.repository;

import com.bage.study.ai.best.practice.dev.flow.entity.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {

    List<Relationship> findByMemberId1(Long memberId1);

    List<Relationship> findByMemberId2(Long memberId2);

    List<Relationship> findByMemberId1OrMemberId2(Long memberId1, Long memberId2);
}