package com.familytree.controller;

import com.familytree.dto.ApiResponse;
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
        // Arrange
        Long familyId = 1L;
        Member member = new Member();
        member.setName("Test Member");
        member.setGender("Male");
        member.setBirthDate(new Date());
        member.setDeathDate(null);
        member.setPhoto("Test Photo");
        member.setDetails("Test Details");
        
        Member createdMember = new Member();
        createdMember.setId(1L);
        createdMember.setName("Test Member");
        createdMember.setGender("Male");
        createdMember.setBirthDate(new Date());
        createdMember.setDeathDate(null);
        createdMember.setPhoto("Test Photo");
        createdMember.setDetails("Test Details");
        
        when(memberService.addMember(
                familyId,
                member.getName(),
                member.getGender(),
                member.getBirthDate(),
                member.getDeathDate(),
                member.getPhoto(),
                member.getDetails(),
                null,
                null
        )).thenReturn(createdMember);
        
        // Act
        ApiResponse<Member> response = memberController.addMember(familyId, member);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(createdMember, response.getData());
        verify(memberService, times(1)).addMember(
                familyId,
                member.getName(),
                member.getGender(),
                member.getBirthDate(),
                member.getDeathDate(),
                member.getPhoto(),
                member.getDetails(),
                null,
                null
        );
    }
    
    @Test
    void testAddMemberFailure() {
        // Arrange
        Long familyId = 1L;
        Member member = new Member();
        member.setName("Test Member");
        member.setGender("Male");
        member.setBirthDate(new Date());
        member.setDeathDate(null);
        member.setPhoto("Test Photo");
        member.setDetails("Test Details");
        
        String errorMessage = "Failed to add member";
        when(memberService.addMember(
                familyId,
                member.getName(),
                member.getGender(),
                member.getBirthDate(),
                member.getDeathDate(),
                member.getPhoto(),
                member.getDetails(),
                null,
                null
        )).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Member> response = memberController.addMember(familyId, member);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(memberService, times(1)).addMember(
                familyId,
                member.getName(),
                member.getGender(),
                member.getBirthDate(),
                member.getDeathDate(),
                member.getPhoto(),
                member.getDetails(),
                null,
                null
        );
    }
    
    @Test
    void testGetMembersSuccess() {
        // Arrange
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
        
        // Act
        ApiResponse<List<Member>> response = memberController.getMembers(familyId);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(members, response.getData());
        verify(memberService, times(1)).getMembersByFamilyId(familyId);
    }
    
    @Test
    void testGetMembersFailure() {
        // Arrange
        Long familyId = 1L;
        String errorMessage = "Failed to get members";
        when(memberService.getMembersByFamilyId(familyId)).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<List<Member>> response = memberController.getMembers(familyId);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(memberService, times(1)).getMembersByFamilyId(familyId);
    }
    
    @Test
    void testGetMemberSuccess() {
        // Arrange
        Long memberId = 1L;
        Member member = new Member();
        member.setId(memberId);
        member.setName("Test Member");
        
        when(memberService.getMemberById(memberId)).thenReturn(member);
        
        // Act
        ApiResponse<Member> response = memberController.getMember(memberId);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(member, response.getData());
        verify(memberService, times(1)).getMemberById(memberId);
    }
    
    @Test
    void testGetMemberFailure() {
        // Arrange
        Long memberId = 1L;
        String errorMessage = "Member not found";
        when(memberService.getMemberById(memberId)).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Member> response = memberController.getMember(memberId);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(memberService, times(1)).getMemberById(memberId);
    }
    
    @Test
    void testUpdateMemberSuccess() {
        // Arrange
        Long memberId = 1L;
        Member member = new Member();
        member.setName("Updated Member");
        member.setGender("Female");
        member.setBirthDate(new Date());
        member.setDeathDate(null);
        member.setPhoto("Updated Photo");
        member.setDetails("Updated Details");
        
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
                member.getName(),
                member.getGender(),
                member.getBirthDate(),
                member.getDeathDate(),
                member.getPhoto(),
                member.getDetails(),
                null,
                null
        )).thenReturn(updatedMember);
        
        // Act
        ApiResponse<Member> response = memberController.updateMember(memberId, member);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(updatedMember, response.getData());
        verify(memberService, times(1)).updateMember(
                memberId,
                member.getName(),
                member.getGender(),
                member.getBirthDate(),
                member.getDeathDate(),
                member.getPhoto(),
                member.getDetails(),
                null,
                null
        );
    }
    
    @Test
    void testUpdateMemberFailure() {
        // Arrange
        Long memberId = 1L;
        Member member = new Member();
        member.setName("Updated Member");
        member.setGender("Female");
        member.setBirthDate(new Date());
        member.setDeathDate(null);
        member.setPhoto("Updated Photo");
        member.setDetails("Updated Details");
        
        String errorMessage = "Failed to update member";
        when(memberService.updateMember(
                memberId,
                member.getName(),
                member.getGender(),
                member.getBirthDate(),
                member.getDeathDate(),
                member.getPhoto(),
                member.getDetails(),
                null,
                null
        )).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Member> response = memberController.updateMember(memberId, member);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(memberService, times(1)).updateMember(
                memberId,
                member.getName(),
                member.getGender(),
                member.getBirthDate(),
                member.getDeathDate(),
                member.getPhoto(),
                member.getDetails(),
                null,
                null
        );
    }
    
    @Test
    void testDeleteMemberSuccess() {
        // Arrange
        Long memberId = 1L;
        doNothing().when(memberService).deleteMember(memberId);
        
        // Act
        ApiResponse<Void> response = memberController.deleteMember(memberId);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNull(response.getData());
        verify(memberService, times(1)).deleteMember(memberId);
    }
    
    @Test
    void testDeleteMemberFailure() {
        // Arrange
        Long memberId = 1L;
        String errorMessage = "Failed to delete member";
        doThrow(new RuntimeException(errorMessage)).when(memberService).deleteMember(memberId);
        
        // Act
        ApiResponse<Void> response = memberController.deleteMember(memberId);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(memberService, times(1)).deleteMember(memberId);
    }
}