package com.bage.my.app.end.point.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bage.my.app.end.point.dto.AppVersionResponse;
import com.bage.my.app.end.point.entity.AppVersion;
import com.bage.my.app.end.point.service.AppVersionService;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/app/")
public class AppUpdateController {

    @Value("${app.update.file-dir:./app-updates}")
    private String fileDir;
    @Autowired
    private AppVersionService appVersionService;

    @PostConstruct
    public void init(){
        appVersionService.initDefaultVersions();
    }


    // 上传文件
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 创建存储目录
            Path uploadPath = Paths.get(fileDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 生成唯一文件名
            String fileId = UUID.randomUUID().toString();
            String fileName = fileId + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // 返回文件ID
            Map<String, String> response = new HashMap<>();
            response.put("fileId", fileId);
            response.put("fileName", file.getOriginalFilename());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    // 下载文件
    @GetMapping("/download/{fileId}")
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
    @GetMapping("/check")
    public ResponseEntity<AppVersionResponse> checkUpdate(@RequestParam String currentVersion) {
        AppVersion appVersion = appVersionService.getLatestVersion();
        String latestVersion = appVersion == null ? "" : appVersion.getVersion();

        if (latestVersion.isEmpty() || latestVersion.equals(currentVersion)) {
            return ResponseEntity.ok(AppVersionResponse.latestVersion(currentVersion));
        } else {
            return ResponseEntity.ok(AppVersionResponse.updateAvailable(appVersion));
        }
    }
}