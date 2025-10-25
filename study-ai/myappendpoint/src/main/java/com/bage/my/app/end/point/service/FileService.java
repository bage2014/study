package com.bage.my.app.end.point.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileService {
    @Value("${app.update.file-dir:./app-updates}")
    private String fileDir;

    /**
     * 上传文件
     * @param file 上传的文件
     * @return 包含文件信息的Map
     * @throws IOException 文件操作异常
     */
    public Map<String, String> uploadFile(MultipartFile file) throws IOException {
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

        // 返回文件信息
        Map<String, String> fileInfo = new HashMap<>();
        fileInfo.put("fileId", fileId);
        fileInfo.put("originalFileName", file.getOriginalFilename());
        fileInfo.put("fileName", fileName);
        fileInfo.put("filePath", filePath.toString());
        
        log.info("文件上传成功 - 文件ID: {}, 文件名: {}", fileId, fileName);
        
        return fileInfo;
    }

    /**
     * 获取文件路径
     * @param fileName 文件名
     * @return 文件路径
     */
    public Path getFilePath(String fileName) {
        Path uploadPath = Paths.get(fileDir);
        return uploadPath.resolve(fileName);
    }

    /**
     * 检查文件是否存在
     * @param fileName 文件名
     * @return 文件是否存在
     */
    public boolean fileExists(String fileName) {
        Path filePath = getFilePath(fileName);
        return Files.exists(filePath) && Files.isRegularFile(filePath);
    }

    /**
     * 删除文件
     * @param fileName 文件名
     * @return 是否删除成功
     */
    public boolean deleteFile(String fileName) {
        try {
            Path filePath = getFilePath(fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("文件删除成功 - 文件名: {}", fileName);
                return true;
            }
            return false;
        } catch (IOException e) {
            log.error("文件删除失败 - 文件名: {}, 错误: {}", fileName, e.getMessage());
            return false;
        }
    }

    /**
     * 获取文件内容类型
     * @param fileName 文件名
     * @return 文件内容类型
     */
    public String getContentType(String fileName) {
        try {
            Path filePath = getFilePath(fileName);
            String contentType = Files.probeContentType(filePath);
            return contentType != null ? contentType : "application/octet-stream";
        } catch (IOException e) {
            log.error("获取文件内容类型失败 - 文件名: {}, 错误: {}", fileName, e.getMessage());
            return "application/octet-stream";
        }
    }

    /**
     * 读取文件字节数据
     * @param fileName 文件名
     * @return 文件字节数据
     * @throws IOException 文件操作异常
     */
    public byte[] readFileBytes(String fileName) throws IOException {
        Path filePath = getFilePath(fileName);
        return Files.readAllBytes(filePath);
    }
}
