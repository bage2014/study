package com.familytree.controller;

import com.familytree.model.Member;
import com.familytree.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/families/{familyId}/members")
public class MemberController {
    @Autowired
    private MemberService memberService;
    
    @PostMapping
    public ResponseEntity<Member> addMember(@PathVariable Long familyId, @RequestBody Member member) {
        Member createdMember = memberService.addMember(
                familyId,
                member.getName(),
                member.getGender(),
                member.getBirthDate(),
                member.getDeathDate(),
                member.getPhoto(),
                member.getDetails()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMember);
    }
    
    @GetMapping
    public ResponseEntity<List<Member>> getMembers(@PathVariable Long familyId) {
        List<Member> members = memberService.getMembersByFamilyId(familyId);
        return ResponseEntity.ok(members);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        Member member = memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member member) {
        Member updatedMember = memberService.updateMember(
                id,
                member.getName(),
                member.getGender(),
                member.getBirthDate(),
                member.getDeathDate(),
                member.getPhoto(),
                member.getDetails()
        );
        return ResponseEntity.ok(updatedMember);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}