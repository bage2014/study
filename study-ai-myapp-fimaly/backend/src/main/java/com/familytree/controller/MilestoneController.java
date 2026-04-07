package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Milestone;
import com.familytree.service.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/milestones")
public class MilestoneController {
    @Autowired
    private MilestoneService milestoneService;

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Milestone>> getMilestonesByMemberId(@PathVariable Long memberId) {
        List<Milestone> milestones = milestoneService.getMilestonesByMemberId(memberId);
        return ResponseEntity.ok(milestones);
    }

    @GetMapping("/member/{memberId}/public")
    public ResponseEntity<List<Milestone>> getPublicMilestonesByMemberId(@PathVariable Long memberId) {
        List<Milestone> milestones = milestoneService.getPublicMilestonesByMemberId(memberId);
        return ResponseEntity.ok(milestones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Milestone> getMilestoneById(@PathVariable Long id) {
        Milestone milestone = milestoneService.getMilestoneById(id)
                .orElseThrow(() -> new RuntimeException("Milestone not found"));
        return ResponseEntity.ok(milestone);
    }

    @PostMapping
    public ResponseEntity<Milestone> createMilestone(@RequestBody Milestone milestone) {
        Milestone createdMilestone = milestoneService.createMilestone(milestone);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMilestone);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Milestone> updateMilestone(@PathVariable Long id, @RequestBody Milestone milestoneDetails) {
        Milestone updatedMilestone = milestoneService.updateMilestone(id, milestoneDetails);
        return ResponseEntity.ok(updatedMilestone);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMilestone(@PathVariable Long id) {
        milestoneService.deleteMilestone(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Milestone deleted successfully"));
    }
}
