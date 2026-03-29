package com.familytree.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "family_id", nullable = false)
    private Long familyId;
    
    @Column(name = "member_id")
    private Long memberId;
    
    @Column(name = "type", nullable = false) // photo, video, document
    private String type;
    
    @Column(name = "url", nullable = false)
    private String url;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "uploaded_by")
    private Long uploadedBy;
    
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadedAt;
    
    @PrePersist
    protected void onCreate() {
        uploadedAt = new Date();
    }
}
