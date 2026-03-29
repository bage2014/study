package com.familytree.repository;

import com.familytree.model.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    List<Relationship> findByMember1Id(Long member1Id);
    List<Relationship> findByMember2Id(Long member2Id);
}
