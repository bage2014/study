package com.familytree.controller;

import com.familytree.dto.*;
import com.familytree.model.Member;
import com.familytree.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberControllerTest {
    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddMemberSuccess() {
        Long familyId = 1L;
        MemberCreateRequest request = new MemberCreateRequest();
        request.setFamilyId(familyId);
        request.setName("Test Member");
        request.setGender("Male");
        request.setBirthDate(new Date());
        request.setDeathDate(null);
        request.setPhoto("Test Photo");
        request.setDetails("Test Details");

        Member createdMember = new Member();
        createdMember.setId(1L);
        createdMember.setFamilyId(familyId);
        createdMember.setName("Test Member");
        createdMember.setGender("Male");
        createdMember.setBirthDate(new Date());
        createdMember.setDeathDate(null);
        createdMember.setPhoto("Test Photo");
        createdMember.setDetails("Test Details");

        when(memberService.addMember(
                request.getFamilyId(),
                request.getName(),
                request.getGender(),
                request.getBirthDate(),
                request.getDeathDate(),
                request.getPhoto(),
                request.getDetails(),
                request.getPhone(),
                request.getEmail()
        )).thenReturn(createdMember);

        ApiResponse<Member> response = memberController.addMember(request);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("成员添加成功", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(createdMember, response.getData());
        verify(memberService, times(1)).addMember(
                request.getFamilyId(),
                request.getName(),
                request.getGender(),
                request.getBirthDate(),
                request.getDeathDate(),
                request.getPhoto(),
                request.getDetails(),
                request.getPhone(),
                request.getEmail()
        );
    }

    @Test
    void testAddMemberFailure() {
        Long familyId = 1L;
        MemberCreateRequest request = new MemberCreateRequest();
        request.setFamilyId(familyId);
        request.setName("Test Member");
        request.setGender("Male");
        request.setBirthDate(new Date());

        String errorMessage = "Failed to add member";
        when(memberService.addMember(
                request.getFamilyId(),
                request.getName(),
                request.getGender(),
                request.getBirthDate(),
                request.getDeathDate(),
                request.getPhoto(),
                request.getDetails(),
                request.getPhone(),
                request.getEmail()
        )).thenThrow(new RuntimeException(errorMessage));

        ApiResponse<Member> response = memberController.addMember(request);

        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(memberService, times(1)).addMember(
                request.getFamilyId(),
                request.getName(),
                request.getGender(),
                request.getBirthDate(),
                request.getDeathDate(),
                request.getPhoto(),
                request.getDetails(),
                request.getPhone(),
                request.getEmail()
        );
    }

    @Test
    void testGetMembersSuccess() {
        Long familyId = 1L;
        List<Member> members = new ArrayList<>();
        Member member1 = new Member();
        member1.setId(1L);
        member1.setName("Member 1");
        members.add(member1);

        Member member2 = new Member();
        member2.setId(2L);
        member2.setName("Member 2");
        members.add(member2);

        when(memberService.getMembersByFamilyId(familyId)).thenReturn(members);

        ApiResponse<List<Member>> response = memberController.getMembers(familyId);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNull(response.getMessage());
        assertNotNull(response.getData());
        assertEquals(members, response.getData());
        verify(memberService, times(1)).getMembersByFamilyId(familyId);
    }

    @Test
    void testGetMembersFailure() {
        Long familyId = 1L;
        String errorMessage = "Failed to get members";
        when(memberService.getMembersByFamilyId(familyId)).thenThrow(new RuntimeException(errorMessage));

        ApiResponse<List<Member>> response = memberController.getMembers(familyId);

        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(memberService, times(1)).getMembersByFamilyId(familyId);
    }

    @Test
    void testGetMemberSuccess() {
        Long memberId = 1L;
        Member member = new Member();
        member.setId(memberId);
        member.setName("Test Member");

        when(memberService.getMemberById(memberId)).thenReturn(member);

        ApiResponse<Member> response = memberController.getMember(memberId);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNull(response.getMessage());
        assertNotNull(response.getData());
        assertEquals(member, response.getData());
        verify(memberService, times(1)).getMemberById(memberId);
    }

    @Test
    void testGetMemberFailure() {
        Long memberId = 1L;
        String errorMessage = "Member not found";
        when(memberService.getMemberById(memberId)).thenThrow(new RuntimeException(errorMessage));

        ApiResponse<Member> response = memberController.getMember(memberId);

        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(memberService, times(1)).getMemberById(memberId);
    }

    @Test
    void testUpdateMemberSuccess() {
        Long memberId = 1L;
        MemberUpdateRequest request = new MemberUpdateRequest();
        request.setName("Updated Member");
        request.setGender("Female");
        request.setBirthDate(new Date());
        request.setDeathDate(null);
        request.setPhoto("Updated Photo");
        request.setDetails("Updated Details");

        Member updatedMember = new Member();
        updatedMember.setId(memberId);
        updatedMember.setName("Updated Member");
        updatedMember.setGender("Female");
        updatedMember.setBirthDate(new Date());
        updatedMember.setDeathDate(null);
        updatedMember.setPhoto("Updated Photo");
        updatedMember.setDetails("Updated Details");

        when(memberService.updateMember(
                memberId,
                request.getName(),
                request.getGender(),
                request.getBirthDate(),
                request.getDeathDate(),
                request.getPhoto(),
                request.getDetails(),
                null,
                null
        )).thenReturn(updatedMember);

        ApiResponse<Member> response = memberController.updateMember(memberId, request);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("成员更新成功", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(updatedMember, response.getData());
        verify(memberService, times(1)).updateMember(
                memberId,
                request.getName(),
                request.getGender(),
                request.getBirthDate(),
                request.getDeathDate(),
                request.getPhoto(),
                request.getDetails(),
                null,
                null
        );
    }

    @Test
    void testUpdateMemberFailure() {
        Long memberId = 1L;
        MemberUpdateRequest request = new MemberUpdateRequest();
        request.setName("Updated Member");
        request.setGender("Female");
        request.setBirthDate(new Date());
        request.setDeathDate(null);
        request.setPhoto("Updated Photo");
        request.setDetails("Updated Details");

        String errorMessage = "Failed to update member";
        when(memberService.updateMember(
                memberId,
                request.getName(),
                request.getGender(),
                request.getBirthDate(),
                request.getDeathDate(),
                request.getPhoto(),
                request.getDetails(),
                null,
                null
        )).thenThrow(new RuntimeException(errorMessage));

        ApiResponse<Member> response = memberController.updateMember(memberId, request);

        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(memberService, times(1)).updateMember(
                memberId,
                request.getName(),
                request.getGender(),
                request.getBirthDate(),
                request.getDeathDate(),
                request.getPhoto(),
                request.getDetails(),
                null,
                null
        );
    }

    @Test
    void testDeleteMemberSuccess() {
        Long memberId = 1L;
        doNothing().when(memberService).deleteMember(memberId);

        ApiResponse<Void> response = memberController.deleteMember(memberId);

        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("成员删除成功", response.getMessage());
        assertNull(response.getData());
        verify(memberService, times(1)).deleteMember(memberId);
    }

    @Test
    void testDeleteMemberFailure() {
        Long memberId = 1L;
        String errorMessage = "Failed to delete member";
        doThrow(new RuntimeException(errorMessage)).when(memberService).deleteMember(memberId);

        ApiResponse<Void> response = memberController.deleteMember(memberId);

        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(memberService, times(1)).deleteMember(memberId);
    }
}