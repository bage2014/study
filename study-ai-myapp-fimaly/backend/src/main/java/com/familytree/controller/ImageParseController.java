package com.familytree.controller;

import com.familytree.dto.ImageParseRequest;
import com.familytree.dto.ImageParseResponse;
import com.familytree.dto.ImageParseSaveRequest;
import com.familytree.dto.ApiResponse;
import com.familytree.service.ImageParseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/api/image-parse")
@RequiredArgsConstructor
@Tag(name = "图片解析", description = "家族关系图图片上传和解析功能")
public class ImageParseController {

    private final ImageParseService imageParseService;

    @PostMapping("/upload")
    @Operation(summary = "上传图片并解析")
    public ApiResponse<ImageParseResponse> uploadAndParse(
            @RequestParam("file") MultipartFile file,
            @RequestParam("familyId") Long familyId) {
        try {
            log.info("[图片解析] 收到图片上传请求，家族ID: {}, 文件名: {}, 大小: {} bytes",
                    familyId, file.getOriginalFilename(), file.getSize());

            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("image/png") && !contentType.equals("image/jpeg"))) {
                return ApiResponse.error("只支持PNG和JPG格式的图片");
            }

            // 验证文件大小（最大10MB）
            if (file.getSize() > 10 * 1024 * 1024) {
                return ApiResponse.error("图片大小不能超过10MB");
            }

            // 将图片转换为Base64
            byte[] fileBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(fileBytes);

            // 调用解析服务
            ImageParseResponse response = imageParseService.parseImage(
                    familyId, base64Image, file.getOriginalFilename());

            if (response.isSuccess()) {
                return ApiResponse.success(response, "图片解析成功");
            } else {
                return ApiResponse.error(response.getMessage());
            }
        } catch (Exception e) {
            log.error("[图片解析] 上传和解析失败", e);
            return ApiResponse.error("图片上传和解析失败: " + e.getMessage());
        }
    }

    @PostMapping("/parse")
    @Operation(summary = "解析Base64图片")
    public ApiResponse<ImageParseResponse> parseImage(@RequestBody ImageParseRequest request) {
        try {
            log.info("[图片解析] 收到Base64图片解析请求，家族ID: {}", request.getFamilyId());

            ImageParseResponse response = imageParseService.parseImage(
                    request.getFamilyId(),
                    request.getImageBase64(),
                    request.getImageName());

            if (response.isSuccess()) {
                return ApiResponse.success(response, "图片解析成功");
            } else {
                return ApiResponse.error(response.getMessage());
            }
        } catch (Exception e) {
            log.error("[图片解析] Base64解析失败", e);
            return ApiResponse.error("图片解析失败: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    @Operation(summary = "保存解析结果到家族")
    public ApiResponse<ImageParseResponse> saveParseResult(@RequestBody ImageParseSaveRequest request) {
        try {
            log.info("[图片解析] 保存解析结果，家族ID: {}, 成员数: {}, 关系数: {}",
                    request.getFamilyId(),
                    request.getMembers().size(),
                    request.getRelationships().size());

            ImageParseResponse response = imageParseService.saveParseResult(
                    request.getFamilyId(),
                    request.getMembers(),
                    request.getRelationships());

            if (response.isSuccess()) {
                return ApiResponse.success(response, "数据保存成功");
            } else {
                return ApiResponse.error(response.getMessage());
            }
        } catch (Exception e) {
            log.error("[图片解析] 保存失败", e);
            return ApiResponse.error("保存失败: " + e.getMessage());
        }
    }
}