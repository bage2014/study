package com.bage.study.ai.best.practice.dev.flow.dto.response;

import lombok.Data;

@Data
public class AiStoryDTO {

    private String storyId;
    private String title;
    private String content;
    private String storyType;
    private String aiComment;
}