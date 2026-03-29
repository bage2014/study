package com.familytree.repository;

import com.familytree.model.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    List<Relationship> findByMemberId1(Long memberId1);
    List<Relationship> findByMemberId2(Long memberId2);
}