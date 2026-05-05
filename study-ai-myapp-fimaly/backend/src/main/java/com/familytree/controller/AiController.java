package com.familytree.controller;

import com.familytree.dto.*;
import com.familytree.service.AiRelationshipService;
import com.familytree.service.FamilyStoryService;
import com.familytree.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI功能", description = "AI关系预测和家族故事生成等功能")
public class AiController {

    private final AiRelationshipService aiRelationshipService;
    private final FamilyStoryService familyStoryService;

    @PostMapping("/relationships/predict/{familyId}")
    @Operation(summary = "AI预测家族成员关系")
    public ApiResponse<RelationshipPredictionResponse> predictRelationships(@PathVariable Long familyId) {
        try {
            RelationshipPredictionResponse response = aiRelationshipService.predictRelationships(familyId);
            return ApiResponse.success(response, "关系预测成功");
        } catch (Exception e) {
            log.error("[AI关系预测] 失败", e);
            return ApiResponse.error("关系预测失败: " + e.getMessage());
        }
    }

    @GetMapping("/relationships/suggestions/{familyId}")
    @Operation(summary = "获取数据补全建议")
    public ApiResponse<List<DataCompletionSuggestion>> getCompletionSuggestions(@PathVariable Long familyId) {
        try {
            List<DataCompletionSuggestion> suggestions = aiRelationshipService.getDataCompletionSuggestions(familyId);
            return ApiResponse.success(suggestions);
        } catch (Exception e) {
            log.error("[AI数据补全] 获取建议失败", e);
            return ApiResponse.error("获取建议失败: " + e.getMessage());
        }
    }

    @PostMapping("/stories/generate/{familyId}")
    @Operation(summary = "生成家族故事")
    public ApiResponse<FamilyStoryResponse> generateFamilyStory(
            @PathVariable Long familyId,
            @RequestBody FamilyStoryRequest request) {
        try {
            FamilyStoryResponse response = familyStoryService.generateFamilyStory(familyId, request);
            return ApiResponse.success(response, "故事生成成功");
        } catch (Exception e) {
            log.error("[AI家族故事] 生成失败", e);
            return ApiResponse.error("故事生成失败: " + e.getMessage());
        }
    }

    @GetMapping("/stories/types")
    @Operation(summary = "获取故事类型列表")
    public ApiResponse<List<String>> getStoryTypes() {
        List<String> types = List.of(
                "migration", "biography", "legend", "default"
        );
        return ApiResponse.success(types);
    }
}
