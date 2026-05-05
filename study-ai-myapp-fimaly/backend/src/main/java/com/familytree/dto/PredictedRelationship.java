package com.familytree.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictedRelationship {
    private PredictedMember member1;
    private PredictedMember member2;
    private String relationship;
    private Double confidence;
    private List<String> reasoning;
    private String aiComment;
    private String status; // pending, confirmed, rejected
}
