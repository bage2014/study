package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Member;
import com.familytree.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/families/{familyId}/members")
public class MemberController {
    @Autowired
    private MemberService memberService;
    
    @PostMapping
    public ApiResponse<Member> addMember(@PathVariable Long familyId, @RequestBody Member member) {
        try {
            Member createdMember = memberService.addMember(
                    familyId,
                    member.getName(),
                    member.getGender(),
                    member.getBirthDate(),
                    member.getDeathDate(),
                    member.getPhoto(),
                    member.getDetails()
            );
            return ApiResponse.success(createdMember);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping
    public ApiResponse<List<Member>> getMembers(@PathVariable Long familyId) {
        try {
            List<Member> members = memberService.getMembersByFamilyId(familyId);
            return ApiResponse.success(members);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Member> getMember(@PathVariable Long id) {
        try {
            Member member = memberService.getMemberById(id);
            return ApiResponse.success(member);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Member> updateMember(@PathVariable Long id, @RequestBody Member member) {
        try {
            Member updatedMember = memberService.updateMember(
                    id,
                    member.getName(),
                    member.getGender(),
                    member.getBirthDate(),
                    member.getDeathDate(),
                    member.getPhoto(),
                    member.getDetails()
            );
            return ApiResponse.success(updatedMember);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMember(@PathVariable Long id) {
        try {
            memberService.deleteMember(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
