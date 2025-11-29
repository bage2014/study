package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.ApiResponse;
import com.bage.my.app.end.point.entity.AppFile;
import com.bage.my.app.end.point.model.response.AppFileListResponse;
import com.bage.my.app.end.point.service.AppFileService;
import com.bage.my.app.end.point.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app/file")
@Slf4j
public class AppFileController {
    
    @Autowired
    private AppFileService appFileService;
    
    // 上传文件
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                log.error("上传文件为空");
                return new ApiResponse<>(400, "上传文件不能为空", null);
            }
            
            log.info("开始上传文件 - 文件名: {}, 大小: {}, 类型: {}", 
                    file.getOriginalFilename(), file.getSize(), file.getContentType());
            
            // 先保存到数据库，再保存到磁盘（在service层实现）
            AppFile savedFile = appFileService.uploadFile(file);
            
            // 返回结果
            Map<String, Object> response = new HashMap<>();
            response.put("fileId", savedFile.getId());
            response.put("fileName", savedFile.getFileName());
            response.put("originalFileName", savedFile.getOriginalFileName());
            response.put("fileSize", savedFile.getFileSize());
            response.put("fileType", savedFile.getFileType());
            response.put("fileUrl", savedFile.getFileUrl());
            response.put("createdTime", savedFile.getCreatedTime());
            
            log.info("文件上传成功 - ID: {}, 文件名: {}", savedFile.getId(), savedFile.getOriginalFileName());
            
            return new ApiResponse<>(200, "文件上传成功", response);
        } catch (IOException e) {
            log.error("文件上传失败 - IO异常: {}", e.getMessage(), e);
            return new ApiResponse<>(500, "文件上传失败: " + e.getMessage(), null);
        } catch (Exception e) {
            log.error("文件上传失败 - 系统异常: {}", e.getMessage(), e);
            return new ApiResponse<>(500, "系统错误: " + e.getMessage(), null);
        }
    }
    
    // 分页查询文件列表
    @RequestMapping("/list")
    public ApiResponse<AppFileListResponse> getFiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        
        log.info("请求参数 - 页码: {}, 每页大小: {}, 关键词: {}", page, size, keyword);
        Page<AppFile> filesPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            log.info("根据关键词搜索文件: {}", keyword);
            filesPage = appFileService.searchFilesByKeyword(keyword, page, size);
        } else {
            filesPage = appFileService.getFilesByPage(page, size);
        }
        
        log.info("文件分页查询结果: {}", JsonUtil.toJson(filesPage.getContent()));
        AppFileListResponse response = new AppFileListResponse(
                filesPage.getContent(),
                filesPage.getTotalElements(),
                filesPage.getTotalPages(),
                filesPage.getNumber(),
                filesPage.getSize()
        );
        
        return ApiResponse.success(response);
    }
    
    // 下载文件
    @GetMapping("/download/{fileName}")
    public void downloadFile(@PathVariable String fileName, HttpServletResponse response) {
        try {
            // 获取文件路径
            Path filePath = appFileService.getFilePath(fileName);
            
            if (!Files.exists(filePath)) {
                throw new RuntimeException("文件不存在");
            }
            
            // 先从数据库查询文件信息
            AppFile file = appFileService.getFilesByPage(0, 100).getContent().stream()
                    .filter(f -> f.getFileName().equals(fileName))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("文件记录不存在"));
            
            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"" + file.getOriginalFileName() + "\"");
            response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            response.setHeader(HttpHeaders.PRAGMA, "no-cache");
            response.setHeader(HttpHeaders.EXPIRES, "0");
            
            // 获取文件大小
            long fileSize = Files.size(filePath);
            response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize));
            
            // 直接写入文件流到响应
            try (InputStream inputStream = Files.newInputStream(filePath);
                 OutputStream outputStream = response.getOutputStream()) {
                
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }
            
            log.info("文件下载成功 - 文件名: {}, 大小: {} bytes", fileName, fileSize);
            
        } catch (IOException e) {
            log.error("文件下载失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件下载失败", e);
        } catch (Exception e) {
            log.error("文件下载失败: {}", e.getMessage(), e);
            throw new RuntimeException("文件下载失败", e);
        }
    }
    
    // 查看文件信息
    @GetMapping("/info/{fileId}")
    public ApiResponse<AppFile> getFileInfo(@PathVariable Long fileId) {
        try {
            AppFile file = appFileService.getFileById(fileId);
            return ApiResponse.success(file);
        } catch (Exception e) {
            log.error("获取文件信息失败: {}", e.getMessage(), e);
            return new ApiResponse<>(500, "获取文件信息失败: " + e.getMessage(), null);
        }
    }
    
    // 删除文件
    @DeleteMapping("/delete/{fileId}")
    public ApiResponse<String> deleteFile(@PathVariable Long fileId) {
        try {
            appFileService.deleteFile(fileId);
            return new ApiResponse<>(200, "文件删除成功", null);
        } catch (IOException e) {
            log.error("文件删除失败 - IO异常: {}", e.getMessage(), e);
            return new ApiResponse<>(500, "文件删除失败: " + e.getMessage(), null);
        } catch (Exception e) {
            log.error("文件删除失败 - 系统异常: {}", e.getMessage(), e);
            return new ApiResponse<>(500, "文件删除失败: " + e.getMessage(), null);
        }
    }
}