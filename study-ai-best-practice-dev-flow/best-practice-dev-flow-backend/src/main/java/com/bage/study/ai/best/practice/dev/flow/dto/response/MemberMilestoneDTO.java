package com.bage.study.ai.best.practice.dev.flow.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberMilestoneDTO {

    private Long id;
    private Long memberId;
    private String memberName;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}