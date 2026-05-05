package com.familytree.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamilyStoryRequest {
    private String familyName;
    private String storyType; // migration, biography, etc.
    private String style;
    private List<String> keywords;
}
