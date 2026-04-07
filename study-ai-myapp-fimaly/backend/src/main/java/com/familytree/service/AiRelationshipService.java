package com.familytree.service;

import com.familytree.model.Member;
import com.familytree.model.Relationship;
import com.familytree.repository.MemberRepository;
import com.familytree.repository.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AiRelationshipService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RelationshipRepository relationshipRepository;

    public List<Relationship> analyzeAndPredictRelationships(Long familyId) {
        // 获取家族所有成员
        List<Member> members = memberRepository.findAll().stream()
                .filter(member -> member.getFamilyId().equals(familyId))
                .collect(Collectors.toList());

        // 获取现有关系
        List<Relationship> existingRelationships = relationshipRepository.findAll();

        // 构建成员ID到成员的映射
        Map<Long, Member> memberMap = members.stream()
                .collect(Collectors.toMap(Member::getId, member -> member));

        // 分析和预测关系
        List<Relationship> predictedRelationships = analyzeRelationships(members, existingRelationships, memberMap, familyId);

        // 保存预测的关系
        for (Relationship relationship : predictedRelationships) {
            relationshipRepository.save(relationship);
        }

        return predictedRelationships;
    }

    private List<Relationship> analyzeRelationships(List<Member> members, List<Relationship> existingRelationships, Map<Long, Member> memberMap, Long familyId) {
        // 这里实现关系分析和预测逻辑
        // 1. 基于年龄差异分析可能的亲子关系
        // 2. 基于姓名分析可能的兄弟姐妹关系
        // 3. 基于婚姻状况分析可能的配偶关系
        
        // 简化实现，实际项目中可能需要更复杂的算法
        return members.stream()
                .flatMap(member1 -> members.stream()
                        .filter(member2 -> !member1.getId().equals(member2.getId()))
                        .filter(member2 -> !hasExistingRelationship(member1.getId(), member2.getId(), existingRelationships))
                        .filter(member2 -> isPotentialRelationship(member1, member2))
                        .map(member2 -> createPredictedRelationship(member1, member2, familyId))
                )
                .collect(Collectors.toList());
    }

    private boolean hasExistingRelationship(Long member1Id, Long member2Id, List<Relationship> existingRelationships) {
        return existingRelationships.stream()
                .anyMatch(rel -> (rel.getMember1Id().equals(member1Id) && rel.getMember2Id().equals(member2Id)) ||
                        (rel.getMember1Id().equals(member2Id) && rel.getMember2Id().equals(member1Id)));
    }

    private boolean isPotentialRelationship(Member member1, Member member2) {
        // 基于年龄差异判断可能的亲子关系
        if (member1.getBirthDate() != null && member2.getBirthDate() != null) {
            int ageDiff = Math.abs(member1.getBirthDate().getYear() - member2.getBirthDate().getYear());
            if (ageDiff >= 18 && ageDiff <= 40) {
                return true;
            }

            // 基于年龄差异判断可能的兄弟姐妹关系
            if (ageDiff <= 10) {
                return true;
            }
        }

        return false;
    }

    private Relationship createPredictedRelationship(Member member1, Member member2, Long familyId) {
        Relationship relationship = new Relationship();
        relationship.setFamilyId(familyId);
        relationship.setMember1Id(member1.getId());
        relationship.setMember2Id(member2.getId());

        // 基于年龄差异确定关系类型
        if (member1.getBirthDate() != null && member2.getBirthDate() != null) {
            int ageDiff = Math.abs(member1.getBirthDate().getYear() - member2.getBirthDate().getYear());
            if (ageDiff >= 18 && ageDiff <= 40) {
                if (member1.getBirthDate().getYear() < member2.getBirthDate().getYear()) {
                    relationship.setRelationshipType("parent");
                } else {
                    relationship.setRelationshipType("child");
                }
            } else if (ageDiff <= 10) {
                relationship.setRelationshipType("sibling");
            } else {
                relationship.setRelationshipType("relative");
            }
        } else {
            relationship.setRelationshipType("relative");
        }

        return relationship;
    }

    public List<Relationship> getPredictedRelationships(Long familyId) {
        // 获取家族所有成员
        List<Member> members = memberRepository.findAll().stream()
                .filter(member -> member.getFamilyId().equals(familyId))
                .collect(Collectors.toList());

        // 获取现有关系
        List<Relationship> existingRelationships = relationshipRepository.findAll();

        // 构建成员ID到成员的映射
        Map<Long, Member> memberMap = members.stream()
                .collect(Collectors.toMap(Member::getId, member -> member));

        // 分析和预测关系
        return analyzeRelationships(members, existingRelationships, memberMap, familyId);
    }
}
