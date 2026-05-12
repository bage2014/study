package com.bage.study.ai.best.practice.dev.flow.service.impl;

import com.bage.study.ai.best.practice.dev.flow.dto.request.MemberRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.MemberDTO;
import com.bage.study.ai.best.practice.dev.flow.entity.Member;
import com.bage.study.ai.best.practice.dev.flow.exception.ResourceNotFoundException;
import com.bage.study.ai.best.practice.dev.flow.repository.FamilyRepository;
import com.bage.study.ai.best.practice.dev.flow.repository.MemberRepository;
import com.bage.study.ai.best.practice.dev.flow.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final FamilyRepository familyRepository;

    @Override
    public MemberDTO createMember(MemberRequest request) {
        log.info("创建成员: familyId={}, name={}", request.getFamilyId(), request.getName());

        if (!familyRepository.existsById(request.getFamilyId())) {
            throw new ResourceNotFoundException("家族不存在");
        }

        Member member = new Member();
        member.setFamilyId(request.getFamilyId());
        member.setName(request.getName());
        member.setGender(request.getGender());
        member.setBirthDate(request.getBirthDate());
        member.setDeathDate(request.getDeathDate());
        member.setPhoto(request.getPhoto());
        member.setDetails(request.getDetails());
        member.setPhone(request.getPhone());
        member.setEmail(request.getEmail());
        member.setOccupation(request.getOccupation());
        member.setEducation(request.getEducation());

        Member savedMember = memberRepository.save(member);
        log.info("成员创建成功: memberId={}", savedMember.getId());

        return convertToDTO(savedMember);
    }

    @Override
    public MemberDTO updateMember(Long memberId, MemberRequest request) {
        log.info("更新成员: memberId={}", memberId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("成员不存在"));

        if (request.getName() != null) {
            member.setName(request.getName());
        }
        if (request.getGender() != null) {
            member.setGender(request.getGender());
        }
        if (request.getBirthDate() != null) {
            member.setBirthDate(request.getBirthDate());
        }
        if (request.getDeathDate() != null) {
            member.setDeathDate(request.getDeathDate());
        }
        if (request.getPhoto() != null) {
            member.setPhoto(request.getPhoto());
        }
        if (request.getDetails() != null) {
            member.setDetails(request.getDetails());
        }
        if (request.getPhone() != null) {
            member.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            member.setEmail(request.getEmail());
        }
        if (request.getOccupation() != null) {
            member.setOccupation(request.getOccupation());
        }
        if (request.getEducation() != null) {
            member.setEducation(request.getEducation());
        }

        Member updatedMember = memberRepository.save(member);
        log.info("成员更新成功: memberId={}", updatedMember.getId());

        return convertToDTO(updatedMember);
    }

    @Override
    public void deleteMember(Long memberId) {
        log.info("删除成员: memberId={}", memberId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("成员不存在"));

        member.setDeleted(1);
        memberRepository.save(member);
        log.info("成员删除成功: memberId={}", memberId);
    }

    @Override
    public MemberDTO getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("成员不存在"));
        return convertToDTO(member);
    }

    @Override
    public List<MemberDTO> getMembersByFamily(Long familyId) {
        return memberRepository.findByFamilyIdAndDeletedFalse(familyId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MemberDTO> searchMembers(String keyword) {
        log.info("搜索成员: keyword={}", keyword);

        List<Member> membersByName = memberRepository.findByNameContaining(keyword);
        
        return membersByName.stream()
                .filter(m -> m.getDeleted() == 0)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MemberDTO convertToDTO(Member member) {
        MemberDTO dto = new MemberDTO();
        dto.setId(member.getId());
        dto.setFamilyId(member.getFamilyId());
        dto.setName(member.getName());
        dto.setGender(member.getGender());
        dto.setBirthDate(member.getBirthDate());
        dto.setDeathDate(member.getDeathDate());
        dto.setPhoto(member.getPhoto());
        dto.setDetails(member.getDetails());
        dto.setPhone(member.getPhone());
        dto.setEmail(member.getEmail());
        dto.setOccupation(member.getOccupation());
        dto.setEducation(member.getEducation());
        dto.setCreatedAt(member.getCreatedAt());
        return dto;
    }
}