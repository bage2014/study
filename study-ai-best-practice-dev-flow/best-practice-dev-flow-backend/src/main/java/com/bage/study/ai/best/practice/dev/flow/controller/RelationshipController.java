package com.bage.study.ai.best.practice.dev.flow.controller;

import com.bage.study.ai.best.practice.dev.flow.dto.RestResult;
import com.bage.study.ai.best.practice.dev.flow.dto.request.RelationshipRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.RelationshipDTO;
import com.bage.study.ai.best.practice.dev.flow.service.RelationshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relationships")
@RequiredArgsConstructor
@Slf4j
public class RelationshipController {

    private final RelationshipService relationshipService;

    @PostMapping
    public ResponseEntity<RestResult<RelationshipDTO>> createRelationship(@Valid @RequestBody RelationshipRequest request) {
        RelationshipDTO relationship = relationshipService.createRelationship(request);
        return ResponseEntity.ok(RestResult.success("关系创建成功", relationship));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResult<Void>> deleteRelationship(@PathVariable Long id) {
        relationshipService.deleteRelationship(id);
        return ResponseEntity.ok(RestResult.success(null));
    }

    @GetMapping("/member")
    public ResponseEntity<RestResult<List<RelationshipDTO>>> getRelationshipsByMember(@RequestParam Long memberId) {
        List<RelationshipDTO> relationships = relationshipService.getRelationshipsByMember(memberId);
        return ResponseEntity.ok(RestResult.success(relationships));
    }

    @GetMapping("/family")
    public ResponseEntity<RestResult<List<RelationshipDTO>>> getRelationshipsByFamily(@RequestParam Long familyId) {
        List<RelationshipDTO> relationships = relationshipService.getRelationshipsByFamily(familyId);
        return ResponseEntity.ok(RestResult.success(relationships));
    }
}