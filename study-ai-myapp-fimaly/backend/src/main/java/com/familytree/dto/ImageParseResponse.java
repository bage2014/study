package com.familytree.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageParseResponse {
    private String parseId;
    private boolean success;
    private String message;
    private List<ParsedMember> members;
    private List<ParsedRelationship> relationships;
    private Integer totalMembers;
    private Integer totalRelationships;
}