package com.bage.my.app.end.point.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

import com.bage.my.app.end.point.enums.VerificationStatus;

@Data
@Entity
public class FamilyRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private FamilyMember member1;
    
    @ManyToOne
    private FamilyMember member2;
    
    @Enumerated(EnumType.STRING)
    private RelationshipType type;

     // 新增认证状态字段
    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    private LocalDate startDate;
    private LocalDate endDate;
}