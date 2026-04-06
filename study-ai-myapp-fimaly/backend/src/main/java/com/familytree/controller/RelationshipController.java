package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Relationship;
import com.familytree.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relationships")
public class RelationshipController {
    @Autowired
    private RelationshipService relationshipService;
    
    @PostMapping
    public ApiResponse<Relationship> createRelationship(@RequestBody Relationship relationship) {
        try {
            Relationship createdRelationship = relationshipService.createRelationship(
                    relationship.getMember1Id(),
                    relationship.getMember2Id(),
                    relationship.getRelationshipType(),
                    relationship.getFamilyId()
            );
            return ApiResponse.success(createdRelationship);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping
    public ApiResponse<List<Relationship>> getRelationships() {
        try {
            List<Relationship> relationships = relationshipService.getRelationships();
            return ApiResponse.success(relationships);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/member/{memberId}")
    public ApiResponse<List<Relationship>> getRelationshipsByMemberId(@PathVariable Long memberId) {
        try {
            List<Relationship> relationships = relationshipService.getRelationshipsByMemberId(memberId);
            return ApiResponse.success(relationships);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRelationship(@PathVariable Long id) {
        try {
            relationshipService.deleteRelationship(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
