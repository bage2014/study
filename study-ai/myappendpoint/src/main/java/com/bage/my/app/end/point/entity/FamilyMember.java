package com.bage.my.app.end.point.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class FamilyMember { 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String avatar;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    private LocalDate birthDate;
    private String birthPlace;
    private String occupation;
    private String education;
    private String contactInfo;
    private boolean deceased;
    private LocalDate deathDate;
    
    @Column(nullable = false)
    private Integer generation = 0;
    
}