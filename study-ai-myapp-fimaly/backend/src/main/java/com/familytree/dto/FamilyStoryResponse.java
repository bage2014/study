package com.familytree.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamilyStoryResponse {
    private String storyId;
    private String title;
    private String content;
    private String storyType;
    private List<String> keywords;
    private String aiModel;
    private Long timestamp;
}
