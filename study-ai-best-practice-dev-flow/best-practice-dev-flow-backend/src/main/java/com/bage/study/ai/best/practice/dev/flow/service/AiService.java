package com.bage.study.ai.best.practice.dev.flow.service;

import com.bage.study.ai.best.practice.dev.flow.dto.response.AiRelationshipPredictionDTO;
import com.bage.study.ai.best.practice.dev.flow.dto.response.AiStoryDTO;

public interface AiService {

    AiRelationshipPredictionDTO predictRelationships(Long familyId);

    AiStoryDTO generateStory(Long familyId, String storyType);
}