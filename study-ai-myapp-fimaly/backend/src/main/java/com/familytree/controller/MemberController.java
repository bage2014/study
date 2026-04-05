package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Member;
import com.familytree.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MemberController {
    @Autowired
    private MemberService memberService;
    
    @PostMapping("/members")
    public ApiResponse<Member> addMember(@RequestBody Member member) {
        try {
            Member createdMember = memberService.addMember(
                    member.getFamilyId(),
                    member.getName(),
                    member.getGender(),
                    member.getBirthDate(),
                    member.getDeathDate(),
                    member.getPhoto(),
                    member.getDetails(),
                    member.getPhone(),
                    member.getEmail()
            );
            return ApiResponse.success(createdMember);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/members/family")
    public ApiResponse<List<Member>> getMembers(@RequestParam Long familyId) {
        try {
            List<Member> members = memberService.getMembersByFamilyId(familyId);
            return ApiResponse.success(members);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/members/{id}")
    public ApiResponse<Member> getMember(@PathVariable Long id) {
        try {
            Member member = memberService.getMemberById(id);
            return ApiResponse.success(member);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/members/{id}")
    public ApiResponse<Member> updateMember(@PathVariable Long id, @RequestBody Member member) {
        try {
            Member updatedMember = memberService.updateMember(
                    id,
                    member.getName(),
                    member.getGender(),
                    member.getBirthDate(),
                    member.getDeathDate(),
                    member.getPhoto(),
                    member.getDetails(),
                    member.getPhone(),
                    member.getEmail()
            );
            return ApiResponse.success(updatedMember);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/members/{id}")
    public ApiResponse<Void> deleteMember(@PathVariable Long id) {
        try {
            memberService.deleteMember(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/members")
    public ApiResponse<List<Member>> getAllMembers() {
        try {
            List<Member> members = memberService.getAllMembers();
            return ApiResponse.success(members);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/members/search")
    public ApiResponse<List<Member>> searchMembers(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email) {
        try {
            List<Member> members = memberService.searchMembers(phone, email);
            return ApiResponse.success(members);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
