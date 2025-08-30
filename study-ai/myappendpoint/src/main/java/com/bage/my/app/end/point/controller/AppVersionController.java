package com.bage.my.app.end.point.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bage.my.app.end.point.dto.AppVersionResponse;
import com.bage.my.app.end.point.entity.AppVersion;
import com.bage.my.app.end.point.entity.ApiResponse;
import com.bage.my.app.end.point.model.response.AppVersionListResponse;
import com.bage.my.app.end.point.service.AppVersionService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/app/")
@Slf4j
public class AppVersionController {

    @Value("${app.update.file-dir:./app-updates}")
    private String fileDir;
    @Autowired
    private AppVersionService appVersionService;

    @PostConstruct
    public void init(){
        appVersionService.initDefaultVersions();
    }

    // 查看版本列表（支持分页）
    @RequestMapping("/versions")
    public ApiResponse<AppVersionListResponse> getVersions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        
        Page<AppVersion> versionsPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            log.info("Searching versions with keyword: {}", keyword);
            versionsPage = appVersionService.searchVersionsByKeyword(keyword, page, size);
        } else {
            versionsPage = appVersionService.getVersionsByPage(page, size);
        }
        
        AppVersionListResponse response = new AppVersionListResponse(
                versionsPage.getContent(),
                versionsPage.getTotalElements(),
                versionsPage.getTotalPages(),
                versionsPage.getNumber(),
                versionsPage.getSize()
        );
        
        return ApiResponse.success(response);
    }

    // 上传文件并保存AppVersion信息
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("version") String version,
            @RequestParam(value = "forceUpdate", defaultValue = "false") boolean forceUpdate,
            @RequestParam(value = "releaseNotes", required = false) String releaseNotes) {
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                log.error("上传文件为空");
                return new ApiResponse<>(400, "上传文件不能为空", null);
            }
            
            // 验证版本号
            if (version == null || version.trim().isEmpty()) {
                log.error("版本号为空");
                return new ApiResponse<>(400, "版本号不能为空", null);
            }
            
            log.info("开始上传文件 - 版本: {}, 强制更新: {}, 更新说明: {}", version, forceUpdate, releaseNotes);
            log.info("文件信息 - 名称: {}, 大小: {}, 类型: {}", 
                    file.getOriginalFilename(), file.getSize(), file.getContentType());
            
            // 创建存储目录
            Path uploadPath = Paths.get(fileDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("创建上传目录: {}", uploadPath);
            }
        
            // 生成唯一文件名
            String fileId = UUID.randomUUID().toString();
            String originalFilename = file.getOriginalFilename();
            String fileName = fileId + "_" + originalFilename;
            Path filePath = uploadPath.resolve(fileName);
            
            log.info("保存文件到: {}", filePath);
            Files.copy(file.getInputStream(), filePath);
        
            // 构建并保存AppVersion
            String downloadUrl = "/app/files/" + fileName;
            AppVersion appVersion = new AppVersion(
                    version, 
                    LocalDate.now(), 
                    releaseNotes != null ? releaseNotes : "", 
                    downloadUrl, 
                    forceUpdate
            );
            appVersion = appVersionService.saveVersion(appVersion);
            
            log.info("版本保存成功 - ID: {}, 版本: {}", appVersion.getId(), appVersion.getVersion());
        
            // 返回结果
            Map<String, Object> response = new HashMap<>();
            response.put("fileId", fileId);
            response.put("originalFileName", originalFilename);
            response.put("fileName", fileName);
            response.put("versionId", appVersion.getId());
            response.put("version", appVersion.getVersion());
            response.put("downloadUrl", appVersion.getDownloadUrl());
            
            return new ApiResponse<>(200, "文件上传和版本保存成功", response);
        } catch (IOException e) {
            log.error("文件上传失败 - IO异常: {}", e.getMessage(), e);
            return new ApiResponse<>(500, "文件上传失败: " + e.getMessage(), null);
        } catch (Exception e) {
            log.error("文件上传失败 - 系统异常: {}", e.getMessage(), e);
            return new ApiResponse<>(500, "系统错误: " + e.getMessage(), null);
        }
    }

    // 下载文件
    @RequestMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        try {
            Path uploadPath = Paths.get(fileDir);
            // 查找匹配的文件
            Path filePath = Files.list(uploadPath)
                    .filter(path -> path.getFileName().toString().startsWith(fileId + "_"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("文件不存在"));

            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName() + "\"")
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }

    // 检查版本更新
    @RequestMapping("/check")
    public ResponseEntity<AppVersionResponse> checkUpdate(@RequestParam String currentVersion) {
        AppVersion appVersion = appVersionService.getLatestVersion();
        String latestVersion = appVersion == null ? "" : appVersion.getVersion();

        if (latestVersion.isEmpty() || latestVersion.equals(currentVersion)) {
            return ResponseEntity.ok(AppVersionResponse.latestVersion(currentVersion));
        } else {
            return ResponseEntity.ok(AppVersionResponse.updateAvailable(appVersion));
        }
    }
    
    // 访问文件
    @RequestMapping("/files/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        try {
            Path uploadPath = Paths.get(fileDir);
            Path filePath = uploadPath.resolve(fileName);
            log.info("文件路径: {}", filePath);
            if (!Files.exists(filePath)) {
                throw new RuntimeException("文件不存在");
            }

            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("文件访问失败: {}", e.getMessage());
            throw new RuntimeException("文件访问失败", e);
        }
    }
}