package com.bage.study.ai.best.practice.dev.flow.controller;

import com.bage.study.ai.best.practice.dev.flow.dto.RestResult;
import com.bage.study.ai.best.practice.dev.flow.dto.response.FamilyTreeDTO;
import com.bage.study.ai.best.practice.dev.flow.service.FamilyTreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/family-tree")
@RequiredArgsConstructor
@Slf4j
public class FamilyTreeController {

    private final FamilyTreeService familyTreeService;

    @GetMapping("/family")
    public ResponseEntity<RestResult<FamilyTreeDTO>> getFamilyTree(@RequestParam Long familyId) {
        FamilyTreeDTO tree = familyTreeService.getFamilyTree(familyId);
        return ResponseEntity.ok(RestResult.success(tree));
    }

    @GetMapping("/member")
    public ResponseEntity<RestResult<FamilyTreeDTO>> getFamilyTreeByMember(@RequestParam Long memberId) {
        FamilyTreeDTO tree = familyTreeService.getFamilyTreeByMember(memberId);
        return ResponseEntity.ok(RestResult.success(tree));
    }
}