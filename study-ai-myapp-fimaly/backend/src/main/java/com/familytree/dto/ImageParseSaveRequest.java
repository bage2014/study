package com.familytree.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageParseSaveRequest {
    private Long familyId;
    private List<ParsedMember> members;
    private List<ParsedRelationship> relationships;
}