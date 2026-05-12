package com.bage.study.ai.best.practice.dev.flow.service.impl;

import com.bage.study.ai.best.practice.dev.flow.dto.response.FamilyTreeDTO;
import com.bage.study.ai.best.practice.dev.flow.entity.Member;
import com.bage.study.ai.best.practice.dev.flow.entity.Relationship;
import com.bage.study.ai.best.practice.dev.flow.exception.ResourceNotFoundException;
import com.bage.study.ai.best.practice.dev.flow.repository.MemberRepository;
import com.bage.study.ai.best.practice.dev.flow.repository.RelationshipRepository;
import com.bage.study.ai.best.practice.dev.flow.service.FamilyTreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FamilyTreeServiceImpl implements FamilyTreeService {

    private final MemberRepository memberRepository;
    private final RelationshipRepository relationshipRepository;

    @Override
    public FamilyTreeDTO getFamilyTree(Long familyId) {
        log.info("获取家族树: familyId={}", familyId);

        List<Member> members = memberRepository.findByFamilyId(familyId).stream()
                .filter(m -> m.getDeleted() == 0)
                .toList();

        List<Relationship> relationships = relationshipRepository.findAll().stream()
                .filter(r -> r.getDeleted() == 0)
                .filter(r -> {
                    Member m1 = memberRepository.findById(r.getMemberId1()).orElse(null);
                    return m1 != null && m1.getFamilyId().equals(familyId);
                })
                .toList();

        return buildFamilyTree(members, relationships);
    }

    @Override
    public FamilyTreeDTO getFamilyTreeByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("成员不存在"));
        
        return getFamilyTree(member.getFamilyId());
    }

    private FamilyTreeDTO buildFamilyTree(List<Member> members, List<Relationship> relationships) {
        Map<Long, FamilyTreeDTO> nodeMap = new HashMap<>();
        Set<Long> childIds = new HashSet<>();

        for (Member member : members) {
            FamilyTreeDTO node = new FamilyTreeDTO();
            node.setMemberId(member.getId());
            node.setName(member.getName());
            node.setGender(member.getGender());
            node.setChildren(new ArrayList<>());
            nodeMap.put(member.getId(), node);
        }

        for (Relationship relationship : relationships) {
            if ("父子".equals(relationship.getRelationType()) || "父女".equals(relationship.getRelationType())) {
                Long parentId = relationship.getMemberId1();
                Long childId = relationship.getMemberId2();
                
                FamilyTreeDTO parent = nodeMap.get(parentId);
                FamilyTreeDTO child = nodeMap.get(childId);
                
                if (parent != null && child != null) {
                    parent.getChildren().add(child);
                    childIds.add(childId);
                }
            } else if ("母子".equals(relationship.getRelationType()) || "母女".equals(relationship.getRelationType())) {
                Long parentId = relationship.getMemberId2();
                Long childId = relationship.getMemberId1();
                
                FamilyTreeDTO parent = nodeMap.get(parentId);
                FamilyTreeDTO child = nodeMap.get(childId);
                
                if (parent != null && child != null) {
                    parent.getChildren().add(child);
                    childIds.add(childId);
                }
            }
        }

        FamilyTreeDTO root = null;
        for (Member member : members) {
            if (!childIds.contains(member.getId())) {
                root = nodeMap.get(member.getId());
                break;
            }
        }

        if (root == null && !members.isEmpty()) {
            root = nodeMap.get(members.get(0).getId());
        }

        return root;
    }
}