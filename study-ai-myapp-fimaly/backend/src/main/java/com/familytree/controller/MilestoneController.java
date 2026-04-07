package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Milestone;
import com.familytree.service.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/milestones")
public class MilestoneController {
    @Autowired
    private MilestoneService milestoneService;

    @GetMapping("/member/{memberId}")
    public ApiResponse<List<Milestone>> getMilestonesByMemberId(@PathVariable Long memberId) {
        try {
            List<Milestone> milestones = milestoneService.getMilestonesByMemberId(memberId);
            return ApiResponse.success(milestones);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/member/{memberId}/public")
    public ApiResponse<List<Milestone>> getPublicMilestonesByMemberId(@PathVariable Long memberId) {
        try {
            List<Milestone> milestones = milestoneService.getPublicMilestonesByMemberId(memberId);
            return ApiResponse.success(milestones);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Milestone> getMilestoneById(@PathVariable Long id) {
        try {
            Milestone milestone = milestoneService.getMilestoneById(id)
                    .orElseThrow(() -> new RuntimeException("Milestone not found"));
            return ApiResponse.success(milestone);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping
    public ApiResponse<Milestone> createMilestone(@RequestBody Milestone milestone) {
        try {
            Milestone createdMilestone = milestoneService.createMilestone(milestone);
            return ApiResponse.success(createdMilestone);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Milestone> updateMilestone(@PathVariable Long id, @RequestBody Milestone milestoneDetails) {
        try {
            Milestone updatedMilestone = milestoneService.updateMilestone(id, milestoneDetails);
            return ApiResponse.success(updatedMilestone);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMilestone(@PathVariable Long id) {
        try {
            milestoneService.deleteMilestone(id);
            return ApiResponse.success(null, "Milestone deleted successfully");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
