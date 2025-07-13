package com.bage.my.app.end.point.service;

import com.bage.my.app.end.point.entity.FamilyMember;
import com.bage.my.app.end.point.entity.FamilyRelationship;
import com.bage.my.app.end.point.repository.FamilyMemberRepository;
import com.bage.my.app.end.point.repository.FamilyRelationshipRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bage.my.app.end.point.model.FamilyMemberTree;

import java.util.*;

@Service
@Slf4j
public class FamilyService {
    
    @Autowired
    private FamilyMemberRepository memberRepository;
    
    @Autowired
    private FamilyRelationshipRepository relationshipRepository;
    
    public void validateRelationship(FamilyRelationship relationship) {
        // 1. 检查成员是否存在
        if (!memberRepository.existsById(relationship.getMember1().getId()) || 
            !memberRepository.existsById(relationship.getMember2().getId())) {
            throw new IllegalArgumentException("成员不存在");
        }
        
        // 2. 检查时间顺序
        if (relationship.getStartDate() != null && relationship.getEndDate() != null && 
            relationship.getStartDate().isAfter(relationship.getEndDate())) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }
        
        // 3. 检查环形关系
        checkCircularRelationship(relationship);
    }
    
    private void checkCircularRelationship(FamilyRelationship relationship) {
        // 使用BFS算法检查环形关系
        Queue<Long> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();
        
        queue.add(relationship.getMember1().getId());
        visited.add(relationship.getMember1().getId());
        
        while (!queue.isEmpty()) {
            Long currentId = queue.poll();
            
            // 获取所有与该成员相关的关系
            List<FamilyRelationship> relationships = relationshipRepository
                .findByMember1IdOrMember2Id(currentId, currentId);
            
            for (FamilyRelationship rel : relationships) {
                Long relatedId = rel.getMember1().getId().equals(currentId) 
                    ? rel.getMember2().getId() 
                    : rel.getMember1().getId();
                
                // 如果发现环形关系
                // todo fix
                // if (relatedId.equals(relationship.getMember1().getId())) {
                //     throw new IllegalArgumentException("检测到环形关系");
                // }
                
                if (!visited.contains(relatedId)) {
                    visited.add(relatedId);
                    queue.add(relatedId);
                }
            }
        }
    }

    public FamilyMember saveMember(FamilyMember member) {
        // 基本校验
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("成员姓名不能为空");
        }
        if (member.getBirthDate() != null && member.getDeathDate() != null 
            && member.getBirthDate().isAfter(member.getDeathDate())) {
            throw new IllegalArgumentException("出生日期不能晚于死亡日期");
        }
        
        return memberRepository.save(member);
    }

    public FamilyMember getMemberById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    public FamilyRelationship saveRelationship(FamilyRelationship relationship) {
        validateRelationship(relationship);
        return relationshipRepository.save(relationship);
    }

    public FamilyMemberTree getFamilyTree(Long rootId, int generations) {
        if (generations <= 0) {
            throw new IllegalArgumentException("代际数必须大于0");
        }
        
        FamilyMember rootMember = getMemberById(rootId);
        if (rootMember == null) {
            throw new IllegalArgumentException("根成员不存在");
        }
        
        return buildTree(rootMember, generations,"");
    }
    
    private FamilyMemberTree buildTree(FamilyMember member, int remainingGenerations, String relationship) {
        FamilyMemberTree node = new FamilyMemberTree(
            member.getId(), 
            member.getName(),
            member.getAvatar(),
            member.getGeneration(),
            relationship
        );
        
        if (remainingGenerations > 0) {
            List<FamilyRelationship> relationships = relationshipRepository
                .findByMember1IdOrMember2Id(member.getId(), member.getId());
                
            for (FamilyRelationship rel : relationships) {
                FamilyMember relatedMember = rel.getMember1().getId().equals(member.getId())
                    ? rel.getMember2() : rel.getMember1();
                    
                String relType = determineRelationshipType(member, relatedMember, rel);
                if(node.getChildren() == null){
                    node.setChildren(new ArrayList<>());
                }
                node.getChildren().add(buildTree(relatedMember, remainingGenerations - 1, relType));
            }
        }
        
        return node;
    }
    
    private String determineRelationshipType(FamilyMember current, FamilyMember related, FamilyRelationship rel) {
        // 实现关系类型判断逻辑
        return "parent"; // 示例返回值
    }
}