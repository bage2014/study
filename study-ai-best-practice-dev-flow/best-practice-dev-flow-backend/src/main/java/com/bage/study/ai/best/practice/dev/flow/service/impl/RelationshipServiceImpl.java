package com.bage.study.ai.best.practice.dev.flow.service.impl;

import com.bage.study.ai.best.practice.dev.flow.dto.request.RelationshipRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.RelationshipDTO;
import com.bage.study.ai.best.practice.dev.flow.entity.Member;
import com.bage.study.ai.best.practice.dev.flow.entity.Relationship;
import com.bage.study.ai.best.practice.dev.flow.exception.ResourceNotFoundException;
import com.bage.study.ai.best.practice.dev.flow.repository.MemberRepository;
import com.bage.study.ai.best.practice.dev.flow.repository.RelationshipRepository;
import com.bage.study.ai.best.practice.dev.flow.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelationshipServiceImpl implements RelationshipService {

    private final RelationshipRepository relationshipRepository;
    private final MemberRepository memberRepository;

    @Override
    public RelationshipDTO createRelationship(RelationshipRequest request) {
        log.info("创建关系: memberId1={}, memberId2={}, type={}", 
                request.getMemberId1(), request.getMemberId2(), request.getRelationType());

        Member member1 = memberRepository.findById(request.getMemberId1())
                .orElseThrow(() -> new ResourceNotFoundException("成员1不存在"));
        Member member2 = memberRepository.findById(request.getMemberId2())
                .orElseThrow(() -> new ResourceNotFoundException("成员2不存在"));

        Relationship relationship = new Relationship();
        relationship.setMemberId1(request.getMemberId1());
        relationship.setMemberId2(request.getMemberId2());
        relationship.setRelationType(request.getRelationType());
        relationship.setDescription(request.getDescription());

        Relationship savedRelationship = relationshipRepository.save(relationship);
        log.info("关系创建成功: relationshipId={}", savedRelationship.getId());

        return convertToDTO(savedRelationship, member1.getName(), member2.getName());
    }

    @Override
    public void deleteRelationship(Long relationshipId) {
        log.info("删除关系: relationshipId={}", relationshipId);

        Relationship relationship = relationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new ResourceNotFoundException("关系不存在"));

        relationship.setDeleted(1);
        relationshipRepository.save(relationship);
        log.info("关系删除成功: relationshipId={}", relationshipId);
    }

    @Override
    public List<RelationshipDTO> getRelationshipsByMember(Long memberId) {
        return relationshipRepository.findByMemberId1OrMemberId2(memberId, memberId).stream()
                .filter(r -> r.getDeleted() == 0)
                .map(r -> {
                    String name1 = memberRepository.findById(r.getMemberId1())
                            .map(Member::getName).orElse("");
                    String name2 = memberRepository.findById(r.getMemberId2())
                            .map(Member::getName).orElse("");
                    return convertToDTO(r, name1, name2);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<RelationshipDTO> getRelationshipsByFamily(Long familyId) {
        return relationshipRepository.findAll().stream()
                .filter(r -> r.getDeleted() == 0)
                .filter(r -> {
                    Member m1 = memberRepository.findById(r.getMemberId1()).orElse(null);
                    return m1 != null && m1.getFamilyId().equals(familyId);
                })
                .map(r -> {
                    String name1 = memberRepository.findById(r.getMemberId1())
                            .map(Member::getName).orElse("");
                    String name2 = memberRepository.findById(r.getMemberId2())
                            .map(Member::getName).orElse("");
                    return convertToDTO(r, name1, name2);
                })
                .collect(Collectors.toList());
    }

    private RelationshipDTO convertToDTO(Relationship relationship, String memberName1, String memberName2) {
        RelationshipDTO dto = new RelationshipDTO();
        dto.setId(relationship.getId());
        dto.setMemberId1(relationship.getMemberId1());
        dto.setMemberName1(memberName1);
        dto.setMemberId2(relationship.getMemberId2());
        dto.setMemberName2(memberName2);
        dto.setRelationType(relationship.getRelationType());
        dto.setDescription(relationship.getDescription());
        dto.setCreatedAt(relationship.getCreatedAt());
        return dto;
    }
}