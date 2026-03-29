package com.familytree.service;

import com.familytree.model.Member;
import com.familytree.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    
    public Member addMember(Long familyId, String name, String gender, Date birthDate, Date deathDate, String photo, String details) {
        Member member = new Member();
        member.setFamilyId(familyId);
        member.setName(name);
        member.setGender(gender);
        member.setBirthDate(birthDate);
        member.setDeathDate(deathDate);
        member.setPhoto(photo);
        member.setDetails(details);
        member.setCreatedAt(new Date());
        return memberRepository.save(member);
    }
    
    public List<Member> getMembersByFamilyId(Long familyId) {
        return memberRepository.findByFamilyId(familyId);
    }
    
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
    }
    
    public Member updateMember(Long memberId, String name, String gender, Date birthDate, Date deathDate, String photo, String details) {
        Member member = getMemberById(memberId);
        if (name != null) member.setName(name);
        if (gender != null) member.setGender(gender);
        if (birthDate != null) member.setBirthDate(birthDate);
        if (deathDate != null) member.setDeathDate(deathDate);
        if (photo != null) member.setPhoto(photo);
        if (details != null) member.setDetails(details);
        return memberRepository.save(member);
    }
    
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}