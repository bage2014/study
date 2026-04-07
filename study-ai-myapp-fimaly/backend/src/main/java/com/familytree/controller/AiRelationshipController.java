package com.familytree.controller;

import com.familytree.model.Relationship;
import com.familytree.service.AiRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/relationships")
public class AiRelationshipController {
    @Autowired
    private AiRelationshipService aiRelationshipService;

    @PostMapping("/analyze/{familyId}")
    public ResponseEntity<List<Relationship>> analyzeRelationships(@PathVariable Long familyId) {
        List<Relationship> predictedRelationships = aiRelationshipService.analyzeAndPredictRelationships(familyId);
        return ResponseEntity.ok(predictedRelationships);
    }

    @GetMapping("/predict/{familyId}")
    public ResponseEntity<List<Relationship>> getPredictedRelationships(@PathVariable Long familyId) {
        List<Relationship> predictedRelationships = aiRelationshipService.getPredictedRelationships(familyId);
        return ResponseEntity.ok(predictedRelationships);
    }
}
