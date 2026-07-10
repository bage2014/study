package com.bage.ai.pipeline.core.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeaturePoint {
    private String id;
    private String title;
    private String description;
    private List<String> acceptanceCriteria;
    private String scope;
}