package com.bage.my.app.end.point.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import com.bage.my.app.end.point.entity.ApiResponse;
import com.bage.my.app.end.point.repository.UserRepository;
import com.bage.my.app.end.point.util.AuthUtil;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import com.bage.my.app.end.point.entity.User;

@RestController
@RequestMapping("/images")
@Slf4j
public class ImageController {

    @Autowired
    private UserRepository userRepository;

    // 图片存储目录
    @Value("${app.update.file-dir:./app-updates}")
    private String fileDir;


    // 上传文件
    @PostMapping("/upload")
    public ApiResponse<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
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


            Long userId = AuthUtil.getCurrentUserId();
            log.info("userId: {}", userId);

            // 仅更新非NULL属性
            User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            existingUser.setAvatarUrl("/images/item/" + fileName);
            User result = userRepository.save(existingUser);
            log.info("result: {}", result);

            // 返回文件ID
            Map<String, String> response = new HashMap<>();
            response.put("fileId", fileId);
            response.put("originalFileName", file.getOriginalFilename());
            response.put("fileName", fileName);
            return new ApiResponse<>(200, "文件上传成功", response);
        } catch (IOException e) {
            log.error("文件上传失败: {}", e.getMessage());
            return new ApiResponse<>(500, "文件上传失败", null);
        }
    }
    /**
     * 访问upload目录下的图片
     * @param fileName 图片文件名
     * @param response HTTP响应
     */
    @GetMapping("/item/{fileName}")
    public void getImage(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        try {
            // 构建图片路径
            Path uploadPath = Paths.get(fileDir);
            Path imagePath = uploadPath.resolve(fileName);

            // 检查文件是否存在
            if (!Files.exists(imagePath) || !Files.isRegularFile(imagePath)) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // 获取文件的媒体类型
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // 设置响应头
            response.setContentType(contentType);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-store");
            response.setDateHeader("Expires", 0);

            // 写入图片数据
            ServletOutputStream out = response.getOutputStream();
            out.write(Files.readAllBytes(imagePath));
            out.flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}