package com.familytree.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "relationships")
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "member_id1", nullable = false)
    private Long memberId1;
    
    @Column(name = "member_id2", nullable = false)
    private Long memberId2;
    
    @Column(name = "relationship_type", nullable = false)
    private String relationshipType;
}