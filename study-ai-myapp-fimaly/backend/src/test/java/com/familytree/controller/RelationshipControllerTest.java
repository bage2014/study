package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Relationship;
import com.familytree.service.RelationshipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RelationshipControllerTest {
    @Mock
    private RelationshipService relationshipService;
    
    @InjectMocks
    private RelationshipController relationshipController;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testCreateRelationshipSuccess() {
        // Arrange
        Long familyId = 1L;
        Relationship relationship = new Relationship();
        relationship.setFamilyId(familyId);
        relationship.setMember1Id(1L);
        relationship.setMember2Id(2L);
        relationship.setRelationshipType("Parent-Child");
        
        Relationship createdRelationship = new Relationship();
        createdRelationship.setId(1L);
        createdRelationship.setFamilyId(familyId);
        createdRelationship.setMember1Id(1L);
        createdRelationship.setMember2Id(2L);
        createdRelationship.setRelationshipType("Parent-Child");
        
        when(relationshipService.createRelationship(
                relationship.getMember1Id(),
                relationship.getMember2Id(),
                relationship.getRelationshipType(),
                relationship.getFamilyId()
        )).thenReturn(createdRelationship);
        
        // Act
        ApiResponse<Relationship> response = relationshipController.createRelationship(relationship);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNull(response.getMessage());
        assertNotNull(response.getData());
        assertEquals(createdRelationship, response.getData());
        verify(relationshipService, times(1)).createRelationship(
                relationship.getMember1Id(),
                relationship.getMember2Id(),
                relationship.getRelationshipType(),
                relationship.getFamilyId()
        );
    }
    
    @Test
    void testCreateRelationshipFailure() {
        // Arrange
        Relationship relationship = new Relationship();
        relationship.setFamilyId(1L);
        relationship.setMember1Id(1L);
        relationship.setMember2Id(2L);
        relationship.setRelationshipType("Parent-Child");
        
        String errorMessage = "Failed to create relationship";
        when(relationshipService.createRelationship(
                relationship.getMember1Id(),
                relationship.getMember2Id(),
                relationship.getRelationshipType(),
                relationship.getFamilyId()
        )).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Relationship> response = relationshipController.createRelationship(relationship);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(relationshipService, times(1)).createRelationship(
                relationship.getMember1Id(),
                relationship.getMember2Id(),
                relationship.getRelationshipType(),
                relationship.getFamilyId()
        );
    }
    
    @Test
    void testGetRelationshipsSuccess() {
        // Arrange
        List<Relationship> relationships = new ArrayList<>();
        Relationship relationship1 = new Relationship();
        relationship1.setId(1L);
        relationship1.setMember1Id(1L);
        relationship1.setMember2Id(2L);
        relationship1.setRelationshipType("Parent-Child");
        relationships.add(relationship1);
        
        Relationship relationship2 = new Relationship();
        relationship2.setId(2L);
        relationship2.setMember1Id(1L);
        relationship2.setMember2Id(3L);
        relationship2.setRelationshipType("Sibling");
        relationships.add(relationship2);
        
        when(relationshipService.getRelationships()).thenReturn(relationships);
        
        // Act
        ApiResponse<List<Relationship>> response = relationshipController.getRelationships();
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNull(response.getMessage());
        assertNotNull(response.getData());
        assertEquals(relationships, response.getData());
        verify(relationshipService, times(1)).getRelationships();
    }
    
    @Test
    void testGetRelationshipsFailure() {
        // Arrange
        String errorMessage = "Failed to get relationships";
        when(relationshipService.getRelationships()).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<List<Relationship>> response = relationshipController.getRelationships();
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(relationshipService, times(1)).getRelationships();
    }
    
    @Test
    void testGetRelationshipsByMemberIdSuccess() {
        // Arrange
        Long memberId = 1L;
        List<Relationship> relationships = new ArrayList<>();
        Relationship relationship1 = new Relationship();
        relationship1.setId(1L);
        relationship1.setMember1Id(memberId);
        relationship1.setMember2Id(2L);
        relationship1.setRelationshipType("Parent-Child");
        relationships.add(relationship1);
        
        Relationship relationship2 = new Relationship();
        relationship2.setId(2L);
        relationship2.setMember1Id(memberId);
        relationship2.setMember2Id(3L);
        relationship2.setRelationshipType("Sibling");
        relationships.add(relationship2);
        
        when(relationshipService.getRelationshipsByMemberId(memberId)).thenReturn(relationships);
        
        // Act
        ApiResponse<List<Relationship>> response = relationshipController.getRelationshipsByMemberId(memberId);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNull(response.getMessage());
        assertNotNull(response.getData());
        assertEquals(relationships, response.getData());
        verify(relationshipService, times(1)).getRelationshipsByMemberId(memberId);
    }
    
    @Test
    void testGetRelationshipsByMemberIdFailure() {
        // Arrange
        Long memberId = 1L;
        String errorMessage = "Failed to get relationships";
        when(relationshipService.getRelationshipsByMemberId(memberId)).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<List<Relationship>> response = relationshipController.getRelationshipsByMemberId(memberId);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(relationshipService, times(1)).getRelationshipsByMemberId(memberId);
    }
    
    @Test
    void testDeleteRelationshipSuccess() {
        // Arrange
        Long relationshipId = 1L;
        doNothing().when(relationshipService).deleteRelationship(relationshipId);
        
        // Act
        ApiResponse<Void> response = relationshipController.deleteRelationship(relationshipId);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNull(response.getMessage());
        assertNull(response.getData());
        verify(relationshipService, times(1)).deleteRelationship(relationshipId);
    }
    
    @Test
    void testDeleteRelationshipFailure() {
        // Arrange
        Long relationshipId = 1L;
        String errorMessage = "Failed to delete relationship";
        doThrow(new RuntimeException(errorMessage)).when(relationshipService).deleteRelationship(relationshipId);
        
        // Act
        ApiResponse<Void> response = relationshipController.deleteRelationship(relationshipId);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(relationshipService, times(1)).deleteRelationship(relationshipId);
    }
}