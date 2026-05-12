package com.bage.study.ai.best.practice.dev.flow.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class AiRelationshipPredictionDTO {

    private String predictionId;
    private List<PredictedRelationship> predictedRelationships;
    private List<String> missingDataSuggestions;

    @Data
    public static class PredictedRelationship {
        private MemberInfo member1;
        private MemberInfo member2;
        private String relationship;
        private double confidence;
        private List<String> reasoning;
        private String aiComment;
    }

    @Data
    public static class MemberInfo {
        private Long id;
        private String name;
        private Integer birthYear;
    }
}