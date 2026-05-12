package com.bage.study.ai.best.practice.dev.flow.controller;

import com.bage.study.ai.best.practice.dev.flow.dto.RestResult;
import com.bage.study.ai.best.practice.dev.flow.dto.response.AiRelationshipPredictionDTO;
import com.bage.study.ai.best.practice.dev.flow.dto.response.AiStoryDTO;
import com.bage.study.ai.best.practice.dev.flow.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Slf4j
public class AiController {

    private final AiService aiService;

    @GetMapping("/predict-relationships")
    public ResponseEntity<RestResult<AiRelationshipPredictionDTO>> predictRelationships(@RequestParam Long familyId) {
        AiRelationshipPredictionDTO prediction = aiService.predictRelationships(familyId);
        return ResponseEntity.ok(RestResult.success(prediction));
    }

    @GetMapping("/generate-story")
    public ResponseEntity<RestResult<AiStoryDTO>> generateStory(
            @RequestParam Long familyId,
            @RequestParam(defaultValue = "general") String storyType) {
        AiStoryDTO story = aiService.generateStory(familyId, storyType);
        return ResponseEntity.ok(RestResult.success(story));
    }
}