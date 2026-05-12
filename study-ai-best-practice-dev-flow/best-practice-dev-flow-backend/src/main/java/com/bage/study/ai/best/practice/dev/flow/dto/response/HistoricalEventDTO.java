package com.bage.study.ai.best.practice.dev.flow.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HistoricalEventDTO {

    private Long id;
    private Long familyId;
    private String name;
    private String description;
    private LocalDate eventDate;
    private String relatedMemberIds;
    private String photo;
    private LocalDateTime createdAt;
}