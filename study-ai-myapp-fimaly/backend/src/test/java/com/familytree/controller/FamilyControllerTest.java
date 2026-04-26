package com.familytree.controller;

import com.familytree.dto.*;
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
        Long userId = 1L;
        FamilyCreateRequest request = new FamilyCreateRequest();
        request.setName("Test Family");
        request.setDescription("Test Description");
        request.setAvatar("Test Avatar");

        Family createdFamily = new Family();
        createdFamily.setId(1L);
        createdFamily.setName("Test Family");
        createdFamily.setDescription("Test Description");
        createdFamily.setAvatar("Test Avatar");

        when(familyService.createFamily(
                request.getName(),
                request.getDescription(),
                request.getAvatar(),
                userId
        )).thenReturn(createdFamily);

        ApiResponse<Family> response = familyController.createFamily(userId, request);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("家族创建成功", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(createdFamily, response.getData());
        verify(familyService, times(1)).createFamily(
                request.getName(),
                request.getDescription(),
                request.getAvatar(),
                userId
        );
    }

    @Test
    void testCreateFamilyFailure() {
        Long userId = 1L;
        FamilyCreateRequest request = new FamilyCreateRequest();
        request.setName("Test Family");
        request.setDescription("Test Description");
        request.setAvatar("Test Avatar");

        String errorMessage = "Failed to create family";
        when(familyService.createFamily(
                request.getName(),
                request.getDescription(),
                request.getAvatar(),
                userId
        )).thenThrow(new RuntimeException(errorMessage));

        ApiResponse<Family> response = familyController.createFamily(userId, request);

        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(familyService, times(1)).createFamily(
                request.getName(),
                request.getDescription(),
                request.getAvatar(),
                userId
        );
    }

    @Test
    void testGetFamiliesSuccess() {
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

        ApiResponse<List<Family>> response = familyController.getFamilies(userId);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNull(response.getMessage());
        assertNotNull(response.getData());
        assertEquals(families, response.getData());
        verify(familyService, times(1)).getFamiliesByCreatorId(userId);
    }

    @Test
    void testGetFamiliesFailure() {
        Long userId = 1L;
        String errorMessage = "Failed to get families";
        when(familyService.getFamiliesByCreatorId(userId)).thenThrow(new RuntimeException(errorMessage));

        ApiResponse<List<Family>> response = familyController.getFamilies(userId);

        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(familyService, times(1)).getFamiliesByCreatorId(userId);
    }

    @Test
    void testGetFamilySuccess() {
        Long familyId = 1L;
        Family family = new Family();
        family.setId(familyId);
        family.setName("Test Family");

        when(familyService.getFamilyById(familyId)).thenReturn(family);

        ApiResponse<Family> response = familyController.getFamily(familyId);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNull(response.getMessage());
        assertNotNull(response.getData());
        assertEquals(family, response.getData());
        verify(familyService, times(1)).getFamilyById(familyId);
    }

    @Test
    void testGetFamilyFailure() {
        Long familyId = 1L;
        String errorMessage = "Family not found";
        when(familyService.getFamilyById(familyId)).thenThrow(new RuntimeException(errorMessage));

        ApiResponse<Family> response = familyController.getFamily(familyId);

        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(familyService, times(1)).getFamilyById(familyId);
    }

    @Test
    void testUpdateFamilySuccess() {
        Long familyId = 1L;
        FamilyUpdateRequest request = new FamilyUpdateRequest();
        request.setName("Updated Family");
        request.setDescription("Updated Description");
        request.setAvatar("Updated Avatar");

        Family updatedFamily = new Family();
        updatedFamily.setId(familyId);
        updatedFamily.setName("Updated Family");
        updatedFamily.setDescription("Updated Description");
        updatedFamily.setAvatar("Updated Avatar");

        when(familyService.updateFamily(
                familyId,
                request.getName(),
                request.getDescription(),
                request.getAvatar()
        )).thenReturn(updatedFamily);

        ApiResponse<Family> response = familyController.updateFamily(familyId, request);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("家族更新成功", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(updatedFamily, response.getData());
        verify(familyService, times(1)).updateFamily(
                familyId,
                request.getName(),
                request.getDescription(),
                request.getAvatar()
        );
    }

    @Test
    void testUpdateFamilyFailure() {
        Long familyId = 1L;
        FamilyUpdateRequest request = new FamilyUpdateRequest();
        request.setName("Updated Family");
        request.setDescription("Updated Description");
        request.setAvatar("Updated Avatar");

        String errorMessage = "Failed to update family";
        when(familyService.updateFamily(
                familyId,
                request.getName(),
                request.getDescription(),
                request.getAvatar()
        )).thenThrow(new RuntimeException(errorMessage));

        ApiResponse<Family> response = familyController.updateFamily(familyId, request);

        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(familyService, times(1)).updateFamily(
                familyId,
                request.getName(),
                request.getDescription(),
                request.getAvatar()
        );
    }

    @Test
    void testDeleteFamilySuccess() {
        Long familyId = 1L;
        doNothing().when(familyService).deleteFamily(familyId);

        ApiResponse<Void> response = familyController.deleteFamily(familyId);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("家族删除成功", response.getMessage());
        assertNull(response.getData());
        verify(familyService, times(1)).deleteFamily(familyId);
    }

    @Test
    void testDeleteFamilyFailure() {
        Long familyId = 1L;
        String errorMessage = "Failed to delete family";
        doThrow(new RuntimeException(errorMessage)).when(familyService).deleteFamily(familyId);

        ApiResponse<Void> response = familyController.deleteFamily(familyId);

        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(familyService, times(1)).deleteFamily(familyId);
    }

    @Test
    void testUpdateAdministrator_Success() {
        Long userId = 1L;
        Long familyId = 1L;
        Long newAdministratorId = 2L;
        Family family = new Family();
        family.setId(familyId);
        family.setName("Test Family");
        family.setAdministratorId(newAdministratorId);

        when(familyService.isAdministrator(familyId, userId)).thenReturn(true);
        when(familyService.updateAdministrator(familyId, newAdministratorId)).thenReturn(family);

        ApiResponse<Family> response = familyController.updateAdministrator(userId, familyId, newAdministratorId);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("管理员更新成功", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(newAdministratorId, response.getData().getAdministratorId());
        verify(familyService, times(1)).isAdministrator(familyId, userId);
        verify(familyService, times(1)).updateAdministrator(familyId, newAdministratorId);
    }

    @Test
    void testUpdateAdministrator_NotAdministrator() {
        Long userId = 2L;
        Long familyId = 1L;
        Long newAdministratorId = 3L;

        when(familyService.isAdministrator(familyId, userId)).thenReturn(false);

        ApiResponse<Family> response = familyController.updateAdministrator(userId, familyId, newAdministratorId);

        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals("只有管理员可以执行此操作", response.getMessage());
        assertNull(response.getData());
        verify(familyService, times(1)).isAdministrator(familyId, userId);
        verify(familyService, never()).updateAdministrator(anyLong(), anyLong());
    }

    @Test
    void testIsAdministrator_True() {
        Long userId = 1L;
        Long familyId = 1L;
        when(familyService.isAdministrator(familyId, userId)).thenReturn(true);

        ApiResponse<Boolean> response = familyController.isAdministrator(userId, familyId);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNull(response.getMessage());
        assertTrue(response.getData());
        verify(familyService, times(1)).isAdministrator(familyId, userId);
    }

    @Test
    void testIsAdministrator_False() {
        Long userId = 2L;
        Long familyId = 1L;
        when(familyService.isAdministrator(familyId, userId)).thenReturn(false);

        ApiResponse<Boolean> response = familyController.isAdministrator(userId, familyId);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNull(response.getMessage());
        assertFalse(response.getData());
        verify(familyService, times(1)).isAdministrator(familyId, userId);
    }
}