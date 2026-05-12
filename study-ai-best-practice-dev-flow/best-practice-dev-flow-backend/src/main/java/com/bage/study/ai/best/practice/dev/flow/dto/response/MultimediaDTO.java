package com.bage.study.ai.best.practice.dev.flow.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MultimediaDTO {

    private Long id;
    private Long familyId;
    private Long memberId;
    private String type;
    private String url;
    private String description;
    private LocalDateTime uploadTime;
    private Long uploaderId;
}