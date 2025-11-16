package com.bage.my.app.end.point.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_file")
@Data
public class AppFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;
    
    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;
    
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    
    @Column(name = "original_file_name", nullable = false, length = 255)
    private String originalFileName;
    
    @Column(name = "file_type", length = 100)
    private String fileType;
    
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
    
    public AppFile() {}
    
    public AppFile(String fileName, String fileUrl, Long fileSize, String originalFileName, String fileType) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.originalFileName = originalFileName;
        this.fileType = fileType;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }
}