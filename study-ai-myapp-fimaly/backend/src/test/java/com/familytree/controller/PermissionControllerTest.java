package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Permission;
import com.familytree.service.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermissionControllerTest {
    @Mock
    private PermissionService permissionService;
    
    @InjectMocks
    private PermissionController permissionController;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testAssignPermissionSuccess() {
        // Arrange
        Permission permission = new Permission();
        permission.setUserId(1L);
        permission.setFamilyId(1L);
        permission.setRole("ADMIN");
        
        Permission createdPermission = new Permission();
        createdPermission.setId(1L);
        createdPermission.setUserId(1L);
        createdPermission.setFamilyId(1L);
        createdPermission.setRole("ADMIN");
        
        when(permissionService.assignPermission(
                permission.getUserId(),
                permission.getFamilyId(),
                permission.getRole()
        )).thenReturn(createdPermission);
        
        // Act
        ApiResponse<Permission> response = permissionController.assignPermission(permission);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(createdPermission, response.getData());
        verify(permissionService, times(1)).assignPermission(
                permission.getUserId(),
                permission.getFamilyId(),
                permission.getRole()
        );
    }
    
    @Test
    void testAssignPermissionFailure() {
        // Arrange
        Permission permission = new Permission();
        permission.setUserId(1L);
        permission.setFamilyId(1L);
        permission.setRole("ADMIN");
        
        String errorMessage = "Failed to assign permission";
        when(permissionService.assignPermission(
                permission.getUserId(),
                permission.getFamilyId(),
                permission.getRole()
        )).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Permission> response = permissionController.assignPermission(permission);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(permissionService, times(1)).assignPermission(
                permission.getUserId(),
                permission.getFamilyId(),
                permission.getRole()
        );
    }
    
    @Test
    void testGetPermissionsSuccess() {
        // Arrange
        List<Permission> permissions = new ArrayList<>();
        Permission permission1 = new Permission();
        permission1.setId(1L);
        permission1.setUserId(1L);
        permission1.setFamilyId(1L);
        permission1.setRole("ADMIN");
        permissions.add(permission1);
        
        Permission permission2 = new Permission();
        permission2.setId(2L);
        permission2.setUserId(2L);
        permission2.setFamilyId(1L);
        permission2.setRole("MEMBER");
        permissions.add(permission2);
        
        when(permissionService.getPermissions()).thenReturn(permissions);
        
        // Act
        ApiResponse<List<Permission>> response = permissionController.getPermissions();
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(permissions, response.getData());
        verify(permissionService, times(1)).getPermissions();
    }
    
    @Test
    void testGetPermissionsFailure() {
        // Arrange
        String errorMessage = "Failed to get permissions";
        when(permissionService.getPermissions()).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<List<Permission>> response = permissionController.getPermissions();
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(permissionService, times(1)).getPermissions();
    }
    
    @Test
    void testUpdatePermissionSuccess() {
        // Arrange
        Long permissionId = 1L;
        Permission permission = new Permission();
        permission.setRole("MEMBER");
        
        Permission updatedPermission = new Permission();
        updatedPermission.setId(permissionId);
        updatedPermission.setRole("MEMBER");
        
        when(permissionService.updatePermission(permissionId, permission.getRole())).thenReturn(updatedPermission);
        
        // Act
        ApiResponse<Permission> response = permissionController.updatePermission(permissionId, permission);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(updatedPermission, response.getData());
        verify(permissionService, times(1)).updatePermission(permissionId, permission.getRole());
    }
    
    @Test
    void testUpdatePermissionFailure() {
        // Arrange
        Long permissionId = 1L;
        Permission permission = new Permission();
        permission.setRole("MEMBER");
        
        String errorMessage = "Failed to update permission";
        when(permissionService.updatePermission(permissionId, permission.getRole())).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Permission> response = permissionController.updatePermission(permissionId, permission);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(permissionService, times(1)).updatePermission(permissionId, permission.getRole());
    }
    
    @Test
    void testDeletePermissionSuccess() {
        // Arrange
        Long permissionId = 1L;
        doNothing().when(permissionService).deletePermission(permissionId);
        
        // Act
        ApiResponse<Void> response = permissionController.deletePermission(permissionId);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNull(response.getData());
        verify(permissionService, times(1)).deletePermission(permissionId);
    }
    
    @Test
    void testDeletePermissionFailure() {
        // Arrange
        Long permissionId = 1L;
        String errorMessage = "Failed to delete permission";
        doThrow(new RuntimeException(errorMessage)).when(permissionService).deletePermission(permissionId);
        
        // Act
        ApiResponse<Void> response = permissionController.deletePermission(permissionId);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(permissionService, times(1)).deletePermission(permissionId);
    }
}