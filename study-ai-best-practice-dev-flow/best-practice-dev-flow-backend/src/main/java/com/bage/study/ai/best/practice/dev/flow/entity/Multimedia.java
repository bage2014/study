package com.bage.study.ai.best.practice.dev.flow.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sys_multimedia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Multimedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "family_id", nullable = false)
    private Long familyId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(nullable = false, length = 512)
    private String url;

    @Column(length = 200)
    private String description;

    @Column(name = "upload_time", nullable = false, updatable = false)
    private LocalDateTime uploadTime;

    @Column(name = "uploader_id")
    private Long uploaderId;

    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer deleted = 0;

    @PrePersist
    protected void onCreate() {
        uploadTime = LocalDateTime.now();
    }
}