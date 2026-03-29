package com.familytree.service;

import com.familytree.model.Relationship;
import com.familytree.repository.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationshipService {
    @Autowired
    private RelationshipRepository relationshipRepository;
    
    public Relationship addRelationship(Long memberId1, Long memberId2, String relationshipType) {
        Relationship relationship = new Relationship();
        relationship.setMemberId1(memberId1);
        relationship.setMemberId2(memberId2);
        relationship.setRelationshipType(relationshipType);
        return relationshipRepository.save(relationship);
    }
    
    public List<Relationship> getRelationshipsByMemberId(Long memberId) {
        List<Relationship> relationships1 = relationshipRepository.findByMemberId1(memberId);
        List<Relationship> relationships2 = relationshipRepository.findByMemberId2(memberId);
        relationships1.addAll(relationships2);
        return relationships1;
    }
    
    public Relationship getRelationshipById(Long relationshipId) {
        return relationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new RuntimeException("Relationship not found"));
    }
    
    public void deleteRelationship(Long relationshipId) {
        relationshipRepository.deleteById(relationshipId);
    }
}