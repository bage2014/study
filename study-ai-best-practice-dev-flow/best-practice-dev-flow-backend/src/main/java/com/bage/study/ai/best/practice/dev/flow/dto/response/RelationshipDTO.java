package com.bage.study.ai.best.practice.dev.flow.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RelationshipDTO {

    private Long id;
    private Long memberId1;
    private String memberName1;
    private Long memberId2;
    private String memberName2;
    private String relationType;
    private String description;
    private LocalDateTime createdAt;
}