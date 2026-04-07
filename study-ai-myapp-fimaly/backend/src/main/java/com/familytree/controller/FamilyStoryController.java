package com.familytree.controller;

import com.familytree.service.FamilyStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stories")
public class FamilyStoryController {
    @Autowired
    private FamilyStoryService familyStoryService;

    @GetMapping("/family/{familyId}")
    public ResponseEntity<String> generateFamilyStory(@PathVariable Long familyId) {
        String story = familyStoryService.generateFamilyStory(familyId);
        return ResponseEntity.ok(story);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<String> generateMemberStory(@PathVariable Long memberId) {
        String story = familyStoryService.generateMemberStory(memberId);
        return ResponseEntity.ok(story);
    }
}
