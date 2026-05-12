package com.bage.study.ai.best.practice.dev.flow.dto;

import java.time.LocalDateTime;

public class TrackPointDTO {

    private Long id;
    private Long userId;
    private Double latitude;
    private Double longitude;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TrackPointDTO() {}

    public static TrackPointDTO fromEntity(com.bage.study.ai.best.practice.dev.flow.entity.TrackPoint entity) {
        TrackPointDTO dto = new TrackPointDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}