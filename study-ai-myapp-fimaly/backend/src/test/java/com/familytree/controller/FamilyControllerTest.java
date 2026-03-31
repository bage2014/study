package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Family;
import com.familytree.service.FamilyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FamilyControllerTest {
    @Mock
    private FamilyService familyService;
    
    @InjectMocks
    private FamilyController familyController;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testCreateFamilySuccess() {
        // Arrange
        Long userId = 1L;
        Family family = new Family();
        family.setName("Test Family");
        family.setDescription("Test Description");
        family.setAvatar("Test Avatar");
        
        Family createdFamily = new Family();
        createdFamily.setId(1L);
        createdFamily.setName("Test Family");
        createdFamily.setDescription("Test Description");
        createdFamily.setAvatar("Test Avatar");
        
        when(familyService.createFamily(
                family.getName(),
                family.getDescription(),
                family.getAvatar(),
                userId
        )).thenReturn(createdFamily);
        
        // Act
        ApiResponse<Family> response = familyController.createFamily(userId, family);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(createdFamily, response.getData());
        verify(familyService, times(1)).createFamily(
                family.getName(),
                family.getDescription(),
                family.getAvatar(),
                userId
        );
    }
    
    @Test
    void testCreateFamilyFailure() {
        // Arrange
        Long userId = 1L;
        Family family = new Family();
        family.setName("Test Family");
        family.setDescription("Test Description");
        family.setAvatar("Test Avatar");
        
        String errorMessage = "Failed to create family";
        when(familyService.createFamily(
                family.getName(),
                family.getDescription(),
                family.getAvatar(),
                userId
        )).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Family> response = familyController.createFamily(userId, family);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(familyService, times(1)).createFamily(
                family.getName(),
                family.getDescription(),
                family.getAvatar(),
                userId
        );
    }
    
    @Test
    void testGetFamiliesSuccess() {
        // Arrange
        Long userId = 1L;
        List<Family> families = new ArrayList<>();
        Family family1 = new Family();
        family1.setId(1L);
        family1.setName("Family 1");
        families.add(family1);
        
        Family family2 = new Family();
        family2.setId(2L);
        family2.setName("Family 2");
        families.add(family2);
        
        when(familyService.getFamiliesByCreatorId(userId)).thenReturn(families);
        
        // Act
        ApiResponse<List<Family>> response = familyController.getFamilies(userId);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(families, response.getData());
        verify(familyService, times(1)).getFamiliesByCreatorId(userId);
    }
    
    @Test
    void testGetFamiliesFailure() {
        // Arrange
        Long userId = 1L;
        String errorMessage = "Failed to get families";
        when(familyService.getFamiliesByCreatorId(userId)).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<List<Family>> response = familyController.getFamilies(userId);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(familyService, times(1)).getFamiliesByCreatorId(userId);
    }
    
    @Test
    void testGetFamilySuccess() {
        // Arrange
        Long familyId = 1L;
        Family family = new Family();
        family.setId(familyId);
        family.setName("Test Family");
        
        when(familyService.getFamilyById(familyId)).thenReturn(family);
        
        // Act
        ApiResponse<Family> response = familyController.getFamily(familyId);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(family, response.getData());
        verify(familyService, times(1)).getFamilyById(familyId);
    }
    
    @Test
    void testGetFamilyFailure() {
        // Arrange
        Long familyId = 1L;
        String errorMessage = "Family not found";
        when(familyService.getFamilyById(familyId)).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Family> response = familyController.getFamily(familyId);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(familyService, times(1)).getFamilyById(familyId);
    }
    
    @Test
    void testUpdateFamilySuccess() {
        // Arrange
        Long familyId = 1L;
        Family family = new Family();
        family.setName("Updated Family");
        family.setDescription("Updated Description");
        family.setAvatar("Updated Avatar");
        
        Family updatedFamily = new Family();
        updatedFamily.setId(familyId);
        updatedFamily.setName("Updated Family");
        updatedFamily.setDescription("Updated Description");
        updatedFamily.setAvatar("Updated Avatar");
        
        when(familyService.updateFamily(
                familyId,
                family.getName(),
                family.getDescription(),
                family.getAvatar()
        )).thenReturn(updatedFamily);
        
        // Act
        ApiResponse<Family> response = familyController.updateFamily(familyId, family);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(updatedFamily, response.getData());
        verify(familyService, times(1)).updateFamily(
                familyId,
                family.getName(),
                family.getDescription(),
                family.getAvatar()
        );
    }
    
    @Test
    void testUpdateFamilyFailure() {
        // Arrange
        Long familyId = 1L;
        Family family = new Family();
        family.setName("Updated Family");
        family.setDescription("Updated Description");
        family.setAvatar("Updated Avatar");
        
        String errorMessage = "Failed to update family";
        when(familyService.updateFamily(
                familyId,
                family.getName(),
                family.getDescription(),
                family.getAvatar()
        )).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Family> response = familyController.updateFamily(familyId, family);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(familyService, times(1)).updateFamily(
                familyId,
                family.getName(),
                family.getDescription(),
                family.getAvatar()
        );
    }
    
    @Test
    void testDeleteFamilySuccess() {
        // Arrange
        Long familyId = 1L;
        doNothing().when(familyService).deleteFamily(familyId);
        
        // Act
        ApiResponse<Void> response = familyController.deleteFamily(familyId);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNull(response.getData());
        verify(familyService, times(1)).deleteFamily(familyId);
    }
    
    @Test
    void testDeleteFamilyFailure() {
        // Arrange
        Long familyId = 1L;
        String errorMessage = "Failed to delete family";
        doThrow(new RuntimeException(errorMessage)).when(familyService).deleteFamily(familyId);
        
        // Act
        ApiResponse<Void> response = familyController.deleteFamily(familyId);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(familyService, times(1)).deleteFamily(familyId);
    }
}