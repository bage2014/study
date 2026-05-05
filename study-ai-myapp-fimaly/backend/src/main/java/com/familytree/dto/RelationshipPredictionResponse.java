package com.familytree.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelationshipPredictionResponse {
    private String predictionId;
    private List<PredictedRelationship> predictedRelationships;
    private List<String> missingDataSuggestions;
    private Integer totalPredictions;
}
