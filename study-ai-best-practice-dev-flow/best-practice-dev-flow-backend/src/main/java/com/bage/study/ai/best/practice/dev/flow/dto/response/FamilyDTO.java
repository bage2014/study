package com.bage.study.ai.best.practice.dev.flow.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FamilyDTO {

    private Long id;
    private String name;
    private String description;
    private String avatar;
    private Long creatorId;
    private String creatorName;
    private Long administratorId;
    private Integer memberCount;
    private LocalDateTime createdAt;
}