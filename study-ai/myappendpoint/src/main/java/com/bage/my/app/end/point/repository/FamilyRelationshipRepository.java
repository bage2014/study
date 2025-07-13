package com.bage.my.app.end.point.repository;

import com.bage.my.app.end.point.entity.FamilyRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FamilyRelationshipRepository extends JpaRepository<FamilyRelationship, Long> {
    
    // 根据成员ID查找关系
    List<FamilyRelationship> findByMember1IdOrMember2Id(Long member1Id, Long member2Id);
    
    // 检查两个成员之间是否存在特定类型的关系
    boolean existsByMember1IdAndMember2IdAndType(Long member1Id, Long member2Id, String type);
}