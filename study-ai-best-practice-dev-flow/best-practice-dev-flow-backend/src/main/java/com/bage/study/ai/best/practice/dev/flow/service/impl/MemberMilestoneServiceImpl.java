package com.bage.study.ai.best.practice.dev.flow.service.impl;

import com.bage.study.ai.best.practice.dev.flow.dto.request.MemberMilestoneRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.MemberMilestoneDTO;
import com.bage.study.ai.best.practice.dev.flow.entity.Member;
import com.bage.study.ai.best.practice.dev.flow.entity.MemberMilestone;
import com.bage.study.ai.best.practice.dev.flow.exception.ResourceNotFoundException;
import com.bage.study.ai.best.practice.dev.flow.repository.MemberMilestoneRepository;
import com.bage.study.ai.best.practice.dev.flow.repository.MemberRepository;
import com.bage.study.ai.best.practice.dev.flow.service.MemberMilestoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberMilestoneServiceImpl implements MemberMilestoneService {

    private final MemberMilestoneRepository milestoneRepository;
    private final MemberRepository memberRepository;

    @Override
    public MemberMilestoneDTO createMilestone(MemberMilestoneRequest request) {
        log.info("创建成员大事件: memberId={}, title={}", request.getMemberId(), request.getTitle());

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("成员不存在"));

        MemberMilestone milestone = new MemberMilestone();
        milestone.setMemberId(request.getMemberId());
        milestone.setTitle(request.getTitle());
        milestone.setContent(request.getContent());

        MemberMilestone savedMilestone = milestoneRepository.save(milestone);
        log.info("成员大事件创建成功: milestoneId={}", savedMilestone.getId());

        return convertToDTO(savedMilestone, member.getName());
    }

    @Override
    public MemberMilestoneDTO updateMilestone(Long milestoneId, MemberMilestoneRequest request) {
        log.info("更新成员大事件: milestoneId={}", milestoneId);

        MemberMilestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("成员大事件不存在"));

        if (request.getTitle() != null) {
            milestone.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            milestone.setContent(request.getContent());
        }

        MemberMilestone updatedMilestone = milestoneRepository.save(milestone);
        
        Member member = memberRepository.findById(updatedMilestone.getMemberId()).orElse(null);
        String memberName = member != null ? member.getName() : "";
        
        log.info("成员大事件更新成功: milestoneId={}", updatedMilestone.getId());

        return convertToDTO(updatedMilestone, memberName);
    }

    @Override
    public void deleteMilestone(Long milestoneId) {
        log.info("删除成员大事件: milestoneId={}", milestoneId);

        MemberMilestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("成员大事件不存在"));

        milestone.setDeleted(1);
        milestoneRepository.save(milestone);
        log.info("成员大事件删除成功: milestoneId={}", milestoneId);
    }

    @Override
    public MemberMilestoneDTO getMilestoneById(Long milestoneId) {
        MemberMilestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("成员大事件不存在"));
        
        Member member = memberRepository.findById(milestone.getMemberId()).orElse(null);
        String memberName = member != null ? member.getName() : "";
        
        return convertToDTO(milestone, memberName);
    }

    @Override
    public List<MemberMilestoneDTO> getMilestonesByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("成员不存在"));
        
        return milestoneRepository.findByMemberId(memberId).stream()
                .filter(m -> m.getDeleted() == 0)
                .map(m -> convertToDTO(m, member.getName()))
                .collect(Collectors.toList());
    }

    private MemberMilestoneDTO convertToDTO(MemberMilestone milestone, String memberName) {
        MemberMilestoneDTO dto = new MemberMilestoneDTO();
        dto.setId(milestone.getId());
        dto.setMemberId(milestone.getMemberId());
        dto.setMemberName(memberName);
        dto.setTitle(milestone.getTitle());
        dto.setContent(milestone.getContent());
        dto.setCreatedAt(milestone.getCreatedAt());
        return dto;
    }
}