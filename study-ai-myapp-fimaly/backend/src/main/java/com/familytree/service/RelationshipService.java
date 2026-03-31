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
    
    public Relationship createRelationship(Long member1Id, Long member2Id, String relationshipType) {
        Relationship relationship = new Relationship();
        relationship.setMember1Id(member1Id);
        relationship.setMember2Id(member2Id);
        relationship.setRelationshipType(relationshipType);
        return relationshipRepository.save(relationship);
    }
    
    public List<Relationship> getRelationships() {
        return relationshipRepository.findAll();
    }
    
    public List<Relationship> getRelationshipsByMemberId(Long memberId) {
        List<Relationship> relationships1 = relationshipRepository.findByMember1Id(memberId);
        List<Relationship> relationships2 = relationshipRepository.findByMember2Id(memberId);
        relationships1.addAll(relationships2);
        return relationships1;
    }
    
    public void deleteRelationship(Long id) {
        relationshipRepository.deleteById(id);
    }
}