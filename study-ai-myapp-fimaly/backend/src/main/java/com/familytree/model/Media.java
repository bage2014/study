package com.familytree.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
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
    
    @Column(name = "type", nullable = false)
    private String type;
    
    @Column(name = "url", nullable = false)
    private String url;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "uploaded_by", nullable = false)
    private Long uploadedBy;
    
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadedAt;
    
    @PrePersist
    protected void onCreate() {
        uploadedAt = new Date();
    }
}