package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.service.FamilyStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stories")
public class FamilyStoryController {
    @Autowired
    private FamilyStoryService familyStoryService;

    @GetMapping("/family/{familyId}")
    public ApiResponse<String> generateFamilyStory(@PathVariable Long familyId) {
        try {
            String story = familyStoryService.generateFamilyStory(familyId);
            return ApiResponse.success(story);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/member/{memberId}")
    public ApiResponse<String> generateMemberStory(@PathVariable Long memberId) {
        try {
            String story = familyStoryService.generateMemberStory(memberId);
            return ApiResponse.success(story);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
