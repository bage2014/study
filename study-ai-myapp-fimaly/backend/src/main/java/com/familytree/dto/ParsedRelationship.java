package com.familytree.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedRelationship {
    private Long member1Id;
    private String member1Name;
    private Long member2Id;
    private String member2Name;
    private String relationshipType;
    private Double confidence;
    private List<String> reasoning;
    private boolean exists;
}