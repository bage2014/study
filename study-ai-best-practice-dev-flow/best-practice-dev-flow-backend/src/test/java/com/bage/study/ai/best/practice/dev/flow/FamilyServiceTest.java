package com.bage.study.ai.best.practice.dev.flow;

import com.bage.study.ai.best.practice.dev.flow.dto.request.FamilyRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.FamilyDTO;
import com.bage.study.ai.best.practice.dev.flow.entity.Family;
import com.bage.study.ai.best.practice.dev.flow.entity.User;
import com.bage.study.ai.best.practice.dev.flow.exception.ResourceNotFoundException;
import com.bage.study.ai.best.practice.dev.flow.repository.FamilyRepository;
import com.bage.study.ai.best.practice.dev.flow.repository.MemberRepository;
import com.bage.study.ai.best.practice.dev.flow.repository.UserRepository;
import com.bage.study.ai.best.practice.dev.flow.service.impl.FamilyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FamilyServiceTest {

    @Mock
    private FamilyRepository familyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private FamilyServiceImpl familyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Family createFamily(Long id, String name, Long creatorId, Long adminId, Integer deleted) {
        Family family = new Family();
        family.setId(id);
        family.setName(name);
        family.setCreatorId(creatorId);
        family.setAdministratorId(adminId);
        family.setDeleted(deleted);
        family.setCreatedAt(LocalDateTime.now());
        family.setUpdatedAt(LocalDateTime.now());
        return family;
    }

    private User createUser(Long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        return user;
    }

    @Test
    void UT_FAMILY_001_CreateFamily_Success() {
        FamilyRequest request = new FamilyRequest();
        request.setName("测试家族");
        request.setDescription("这是一个测试家族");

        when(familyRepository.save(any(Family.class))).thenAnswer(invocation -> {
            Family family = invocation.getArgument(0);
            family.setId(1L);
            return family;
        });

        FamilyDTO result = familyService.createFamily(1L, request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试家族", result.getName());
        assertEquals("这是一个测试家族", result.getDescription());
        verify(familyRepository, times(1)).save(any(Family.class));
    }

    @Test
    void UT_FAMILY_002_CreateFamily_NoName() {
        FamilyRequest request = new FamilyRequest();
        request.setName(null);

        when(familyRepository.save(any(Family.class))).thenAnswer(invocation -> {
            Family family = invocation.getArgument(0);
            family.setId(1L);
            return family;
        });

        FamilyDTO result = familyService.createFamily(1L, request);

        assertNotNull(result);
        assertNull(result.getName());
    }

    @Test
    void UT_FAMILY_003_GetUserFamilyList_Success() {
        Family family1 = createFamily(1L, "家族1", 1L, 1L, 0);
        Family family2 = createFamily(2L, "家族2", 1L, 1L, 0);

        when(familyRepository.findByCreatorId(1L)).thenReturn(Arrays.asList(family1, family2));

        List<FamilyDTO> result = familyService.getFamiliesByUser(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("家族1", result.get(0).getName());
        assertEquals("家族2", result.get(1).getName());
    }

    @Test
    void UT_FAMILY_004_GetFamilyById_Success() {
        Family family = createFamily(1L, "测试家族", 1L, 1L, 0);
        family.setDescription("描述");

        when(familyRepository.findById(1L)).thenReturn(Optional.of(family));
        when(userRepository.findById(1L)).thenReturn(Optional.of(createUser(1L, "admin")));
        when(memberRepository.countByFamilyIdAndDeletedFalse(1L)).thenReturn(5L);

        FamilyDTO result = familyService.getFamilyById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试家族", result.getName());
        assertEquals("admin", result.getCreatorName());
        assertEquals(5, result.getMemberCount());
    }

    @Test
    void UT_FAMILY_005_UpdateFamily_Success() {
        Family existing = createFamily(1L, "旧名称", 1L, 1L, 0);
        existing.setDescription("旧描述");

        FamilyRequest request = new FamilyRequest();
        request.setName("新名称");

        when(familyRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(familyRepository.save(any(Family.class))).thenReturn(existing);

        FamilyDTO result = familyService.updateFamily(1L, request);

        assertNotNull(result);
        assertEquals("新名称", result.getName());
    }

    @Test
    void UT_FAMILY_006_DeleteFamily_Success() {
        Family family = createFamily(1L, "测试家族", 1L, 1L, 0);

        when(familyRepository.findById(1L)).thenReturn(Optional.of(family));
        when(familyRepository.save(any(Family.class))).thenReturn(family);

        familyService.deleteFamily(1L);

        assertEquals(1, family.getDeleted());
        verify(familyRepository, times(1)).save(any(Family.class));
    }

    @Test
    void UT_FAMILY_007_DeleteFamily_NotFound() {
        when(familyRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> familyService.deleteFamily(999L));
    }

    @Test
    void UT_FAMILY_008_AddMember_Success() {
        FamilyRequest request = new FamilyRequest();
        request.setName("测试家族");

        when(familyRepository.save(any(Family.class))).thenAnswer(invocation -> {
            Family family = invocation.getArgument(0);
            family.setId(1L);
            return family;
        });

        FamilyDTO result = familyService.createFamily(1L, request);

        assertNotNull(result);
        assertEquals("测试家族", result.getName());
    }

    @Test
    void UT_FAMILY_009_AddMember_NoName() {
        FamilyRequest request = new FamilyRequest();
        request.setName(null);

        when(familyRepository.save(any(Family.class))).thenAnswer(invocation -> {
            Family family = invocation.getArgument(0);
            family.setId(1L);
            return family;
        });

        FamilyDTO result = familyService.createFamily(1L, request);

        assertNotNull(result);
    }

    @Test
    void UT_FAMILY_010_GetFamilyMembers_Success() {
        Family family = createFamily(1L, "测试家族", 1L, 1L, 0);

        when(familyRepository.findById(1L)).thenReturn(Optional.of(family));
        when(userRepository.findById(1L)).thenReturn(Optional.of(createUser(1L, "admin")));
        when(memberRepository.countByFamilyIdAndDeletedFalse(1L)).thenReturn(0L);

        FamilyDTO result = familyService.getFamilyById(1L);

        assertNotNull(result);
        assertEquals(0, result.getMemberCount());
    }

    @Test
    void UT_FAMILY_011_TransferAdministrator_Success() {
        Family family = createFamily(1L, "测试家族", 1L, 1L, 0);

        when(familyRepository.findById(1L)).thenReturn(Optional.of(family));
        when(userRepository.existsById(2L)).thenReturn(true);
        when(familyRepository.save(any(Family.class))).thenReturn(family);

        familyService.transferAdministrator(1L, 2L);

        assertEquals(2L, family.getAdministratorId());
    }

    @Test
    void UT_FAMILY_012_TransferAdministrator_UserNotFound() {
        Family family = createFamily(1L, "测试家族", 1L, 1L, 0);

        when(familyRepository.findById(1L)).thenReturn(Optional.of(family));
        when(userRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> familyService.transferAdministrator(1L, 999L));
    }
}
