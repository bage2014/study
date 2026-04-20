package com.familytree.service;

import com.familytree.model.Family;
import com.familytree.repository.FamilyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FamilyServiceTest {

    @Mock
    private FamilyRepository familyRepository;

    @InjectMocks
    private FamilyService familyService;

    private Family testFamily;

    @BeforeEach
    void setUp() {
        testFamily = new Family();
        testFamily.setId(1L);
        testFamily.setName("Test Family");
        testFamily.setDescription("Test Description");
        testFamily.setCreatorId(1L);
        testFamily.setAdministratorId(1L);
    }

    @Test
    void testCreateFamily_ShouldSetCreatorAsAdministrator() {
        Long creatorId = 1L;
        when(familyRepository.save(any(Family.class))).thenAnswer(invocation -> {
            Family family = invocation.getArgument(0);
            family.setId(1L);
            return family;
        });

        Family createdFamily = familyService.createFamily("New Family", "Description", null, creatorId);

        assertNotNull(createdFamily);
        assertEquals(creatorId, createdFamily.getCreatorId());
        assertEquals(creatorId, createdFamily.getAdministratorId());
        verify(familyRepository, times(1)).save(any(Family.class));
    }

    @Test
    void testUpdateAdministrator_Success() {
        Long familyId = 1L;
        Long newAdminId = 2L;
        when(familyRepository.findById(familyId)).thenReturn(Optional.of(testFamily));
        when(familyRepository.save(any(Family.class))).thenReturn(testFamily);

        Family updatedFamily = familyService.updateAdministrator(familyId, newAdminId);

        assertNotNull(updatedFamily);
        assertEquals(newAdminId, updatedFamily.getAdministratorId());
        verify(familyRepository, times(1)).findById(familyId);
        verify(familyRepository, times(1)).save(any(Family.class));
    }

    @Test
    void testUpdateAdministrator_FamilyNotFound() {
        Long familyId = 999L;
        Long newAdminId = 2L;
        when(familyRepository.findById(familyId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            familyService.updateAdministrator(familyId, newAdminId);
        });

        verify(familyRepository, times(1)).findById(familyId);
        verify(familyRepository, never()).save(any(Family.class));
    }

    @Test
    void testIsAdministrator_WhenUserIsAdministrator() {
        Long familyId = 1L;
        Long userId = 1L;
        when(familyRepository.findById(familyId)).thenReturn(Optional.of(testFamily));

        boolean result = familyService.isAdministrator(familyId, userId);

        assertTrue(result);
        verify(familyRepository, times(1)).findById(familyId);
    }

    @Test
    void testIsAdministrator_WhenUserIsNotAdministrator() {
        Long familyId = 1L;
        Long userId = 2L;
        when(familyRepository.findById(familyId)).thenReturn(Optional.of(testFamily));

        boolean result = familyService.isAdministrator(familyId, userId);

        assertFalse(result);
        verify(familyRepository, times(1)).findById(familyId);
    }

    @Test
    void testIsAdministrator_FamilyNotFound() {
        Long familyId = 999L;
        Long userId = 1L;
        when(familyRepository.findById(familyId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            familyService.isAdministrator(familyId, userId);
        });

        verify(familyRepository, times(1)).findById(familyId);
    }

    @Test
    void testGetFamilyById_Success() {
        Long familyId = 1L;
        when(familyRepository.findById(familyId)).thenReturn(Optional.of(testFamily));

        Family foundFamily = familyService.getFamilyById(familyId);

        assertNotNull(foundFamily);
        assertEquals(familyId, foundFamily.getId());
        verify(familyRepository, times(1)).findById(familyId);
    }

    @Test
    void testGetFamilyById_NotFound() {
        Long familyId = 999L;
        when(familyRepository.findById(familyId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            familyService.getFamilyById(familyId);
        });

        verify(familyRepository, times(1)).findById(familyId);
    }
}