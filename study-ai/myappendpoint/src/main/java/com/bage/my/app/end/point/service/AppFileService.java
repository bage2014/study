package com.bage.my.app.end.point.service;

import com.bage.my.app.end.point.entity.AppFile;
import com.bage.my.app.end.point.repository.AppFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class AppFileService {
    
    @Value("${app.update.file-dir:./uploads}")
    private String uploadDir;
    
    @Autowired
    private AppFileRepository appFileRepository;
    
    // 上传文件（先写入表，再上传文件）
    public AppFile uploadFile(MultipartFile multipartFile) throws IOException {
        // 验证文件
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        
        // 生成唯一文件名
        String fileId = UUID.randomUUID().toString();
        String originalFileName = multipartFile.getOriginalFilename();
        String fileName = fileId + "_" + originalFileName;
        
        // 构建文件URL
        String fileUrl = "/file/download/" + fileName;
        
        // 先创建文件记录
        AppFile appFile = new AppFile(
            fileName,
            fileUrl,
            multipartFile.getSize(),
            originalFileName,
            multipartFile.getContentType()
        );
        
        // 保存到数据库
        AppFile savedFile = appFileRepository.save(appFile);
        log.info("文件记录已保存到数据库 - ID: {}, 文件名: {}", savedFile.getId(), savedFile.getOriginalFileName());
        
        // 再创建存储目录并保存文件到磁盘
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("创建上传目录: {}", uploadPath);
        }
        
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(multipartFile.getInputStream(), filePath);
        log.info("文件已保存到磁盘: {}", filePath);
        
        return savedFile;
    }
    
    // 分页获取文件列表
    public Page<AppFile> getFilesByPage(int page, int size) {
        // 参数验证和默认值设置
        if (page < 0) {
            log.warn("页码不能为负数，已自动设置为0。输入页码: {}", page);
            page = 0;
        }
        
        if (size <= 0) {
            log.warn("每页大小必须大于0，已自动设置为10。输入大小: {}", size);
            size = 10;
        }
        
        // 限制最大页面大小，防止内存溢出
        int maxPageSize = 100;
        if (size > maxPageSize) {
            log.warn("每页大小超过最大限制{}，已自动设置为最大值。输入大小: {}", maxPageSize, size);
            size = maxPageSize;
        }
        
        // 创建分页请求，按照创建时间倒序排列（最新文件在前）
        Pageable pageable = PageRequest.of(page, size, 
                org.springframework.data.domain.Sort.by("createdTime").descending());
        Page<AppFile> result = appFileRepository.findAll(pageable);
        
        log.info("文件分页查询结果 - JPA页码: {}, 每页大小: {}, 总记录数: {}, 总页数: {}, 当前页记录数: {}", 
                page, size, result.getTotalElements(), result.getTotalPages(), result.getNumberOfElements());
        
        return result;
    }
    
    // 根据关键词分页搜索文件
    public Page<AppFile> searchFilesByKeyword(String keyword, int page, int size) {
        // 参数验证
        if (page < 0) {
            log.warn("页码不能为负数，已自动设置为0。输入页码: {}", page);
            page = 0;
        }
        
        if (size <= 0) {
            log.warn("每页大小必须大于0，已自动设置为10。输入大小: {}", size);
            size = 10;
        }
        
        // 限制最大页面大小，防止内存溢出
        int maxPageSize = 100;
        if (size > maxPageSize) {
            log.warn("每页大小超过最大限制{}，已自动设置为最大值。输入大小: {}", maxPageSize, size);
            size = maxPageSize;
        }
        
        // 创建分页请求，按照创建时间倒序排列（最新文件在前）
        Pageable pageable = PageRequest.of(page, size, 
                org.springframework.data.domain.Sort.by("createdTime").descending());
        Page<AppFile> result = appFileRepository.findByKeyword(keyword, pageable);
        
        log.info("文件搜索分页查询结果 - JPA页码: {}, 每页大小: {}, 关键词: {}, 总记录数: {}, 总页数: {}, 当前页记录数: {}", 
                page, size, keyword, result.getTotalElements(), result.getTotalPages(), result.getNumberOfElements());
        
        return result;
    }
    
    // 根据ID获取文件
    public AppFile getFileById(Long id) {
        return appFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("文件不存在，ID: " + id));
    }
    
    // 根据文件名获取文件路径
    public Path getFilePath(String fileName) {
        return Paths.get(uploadDir).resolve(fileName);
    }
    
    // 删除文件
    public void deleteFile(Long id) throws IOException {
        AppFile file = getFileById(id);
        
        // 删除磁盘文件
        Path filePath = getFilePath(file.getFileName());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
            log.info("删除磁盘文件: {}", filePath);
        }
        
        // 删除数据库记录
        appFileRepository.deleteById(id);
        log.info("删除文件记录 - ID: {}, 文件名: {}", id, file.getOriginalFileName());
    }
}