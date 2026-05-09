package com.familytree.controller;

import com.familytree.dto.AvatarUploadResponse;
import com.familytree.service.AvatarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@RestController
@RequestMapping("/api/avatars")
@RequiredArgsConstructor
@Tag(name = "头像管理", description = "头像上传和访问接口")
public class AvatarController {

    private final AvatarService avatarService;

    @PostMapping("/upload")
    @Operation(summary = "上传头像")
    public ResponseEntity<AvatarUploadResponse> uploadAvatar(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "头像文件") @RequestParam("file") MultipartFile file) {
        log.info("[上传头像] 用户ID: {}, 文件名: {}, 大小: {} bytes", userId, file.getOriginalFilename(), file.getSize());
        try {
            String avatarUrl = avatarService.uploadAvatar(userId, file);
            return ResponseEntity.ok(AvatarUploadResponse.success(avatarUrl));
        } catch (RuntimeException e) {
            log.warn("[上传头像] 失败: {}", e.getMessage());
            return ResponseEntity.badRequest().body(AvatarUploadResponse.error(e.getMessage()));
        } catch (IOException e) {
            log.error("[上传头像] 失败", e);
            return ResponseEntity.internalServerError().body(AvatarUploadResponse.error("上传失败"));
        }
    }

    @GetMapping("/{filename}")
    @Operation(summary = "获取头像（需登录）")
    public ResponseEntity<Resource> getAvatar(
            @Parameter(hidden = true) @RequestAttribute("userId") Long userId,
            @Parameter(description = "头像文件名") @PathVariable String filename) {
        log.info("[获取头像] 用户ID: {}, 文件名: {}", userId, filename);
        try {
            Path filePath = avatarService.getAvatarPath(filename);
            if (!Files.exists(filePath)) {
                log.warn("[获取头像] 文件不存在: {}", filename);
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(filePath);
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "image/png";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=3600")
                    .body(resource);
        } catch (IOException e) {
            log.error("[获取头像] 失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}