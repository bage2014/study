package com.bage.my.app.end.point.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

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
    
    private LocalDate startDate;
    private LocalDate endDate;
}