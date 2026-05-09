package com.familytree.service;

import com.familytree.model.User;
import com.familytree.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AvatarService {

    private final UserRepository userRepository;

    @Value("${app.avatar.upload-dir:./uploads/avatars}")
    private String uploadDir;

    @Value("${app.avatar.base-url:/api/avatars}")
    private String avatarBaseUrl;

    /**
     * 上传头像并更新用户信息
     * @param userId 用户ID
     * @param file 头像文件
     * @return 头像URL
     */
    public String uploadAvatar(Long userId, MultipartFile file) throws IOException {
        // 验证文件
        validateFile(file);

        // 创建上传目录
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String newFilename = UUID.randomUUID().toString() + "." + extension;
        Path filePath = uploadPath.resolve(newFilename);

        // 删除旧头像
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        deleteOldAvatar(user.getAvatar());

        // 保存新头像
        Files.copy(file.getInputStream(), filePath);

        // 构建头像URL
        String avatarUrl = avatarBaseUrl + "/" + newFilename;

        // 更新用户头像
        user.setAvatar(avatarUrl);
        userRepository.save(user);

        log.info("[头像上传] 用户ID: {}, 头像URL: {}", userId, avatarUrl);
        return avatarUrl;
    }

    /**
     * 获取头像文件路径
     * @param filename 文件名
     * @return 文件路径
     */
    public Path getAvatarPath(String filename) {
        return Paths.get(uploadDir, filename);
    }

    /**
     * 验证上传文件
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("请选择要上传的文件");
        }

        String contentType = file.getContentType();
        if (contentType == null || !isValidImageType(contentType)) {
            throw new RuntimeException("只支持JPG、PNG、GIF格式的图片");
        }

        // 限制文件大小为5MB
        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new RuntimeException("图片大小不能超过5MB");
        }
    }

    /**
     * 判断是否为有效的图片类型
     */
    private boolean isValidImageType(String contentType) {
        return contentType.equals("image/jpeg") || 
               contentType.equals("image/jpg") || 
               contentType.equals("image/png") || 
               contentType.equals("image/gif");
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "png";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 删除旧头像文件
     */
    private void deleteOldAvatar(String avatarUrl) {
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            try {
                String filename = avatarUrl.substring(avatarUrl.lastIndexOf("/") + 1);
                Path oldFilePath = Paths.get(uploadDir, filename);
                Files.deleteIfExists(oldFilePath);
                log.info("[头像上传] 删除旧头像: {}", oldFilePath);
            } catch (IOException e) {
                log.warn("[头像上传] 删除旧头像失败", e);
            }
        }
    }
}