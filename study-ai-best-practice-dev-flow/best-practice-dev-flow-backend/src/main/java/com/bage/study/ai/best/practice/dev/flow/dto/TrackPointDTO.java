package com.bage.study.ai.best.practice.dev.flow.dto;

import com.bage.study.ai.best.practice.dev.flow.entity.TrackPoint;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackPointDTO {

    private Long id;
    private Long userId;
    private Double latitude;
    private Double longitude;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TrackPointDTO fromEntity(TrackPoint entity) {
        return TrackPointDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}