package com.familytree.controller;

import com.familytree.model.Relationship;
import com.familytree.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relationships")
public class RelationshipController {
    @Autowired
    private RelationshipService relationshipService;
    
    @PostMapping
    public ResponseEntity<Relationship> addRelationship(@RequestBody Relationship relationship) {
        Relationship createdRelationship = relationshipService.addRelationship(
                relationship.getMemberId1(),
                relationship.getMemberId2(),
                relationship.getRelationshipType()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRelationship);
    }
    
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Relationship>> getRelationshipsByMemberId(@PathVariable Long memberId) {
        List<Relationship> relationships = relationshipService.getRelationshipsByMemberId(memberId);
        return ResponseEntity.ok(relationships);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelationship(@PathVariable Long id) {
        relationshipService.deleteRelationship(id);
        return ResponseEntity.noContent().build();
    }
}