package com.bage.study.ai.dev.flow.dto.response;

import com.bage.study.ai.dev.flow.entity.SampleEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SampleResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SampleResponse fromEntity(SampleEntity entity) {
        SampleResponse response = new SampleResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}
