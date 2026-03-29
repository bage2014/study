package com.familytree.service;

import com.familytree.model.Media;
import com.familytree.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class MediaService {
    @Autowired
    private MediaRepository mediaRepository;
    
    public Media uploadMedia(MultipartFile file, Long familyId, String description) {
        try {
            // 这里应该实现文件上传逻辑，暂时模拟
            String fileName = file.getOriginalFilename();
            String url = "/uploads/" + fileName;
            
            Media media = new Media();
            media.setFamilyId(familyId);
            media.setType(getFileType(fileName));
            media.setUrl(url);
            media.setDescription(description);
            media.setUploadedAt(new Date());
            
            return mediaRepository.save(media);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload media: " + e.getMessage());
        }
    }
    
    public Media uploadMedia(Long familyId, String type, String url, String description, Long uploadedBy) {
        Media media = new Media();
        media.setFamilyId(familyId);
        media.setType(type);
        media.setUrl(url);
        media.setDescription(description);
        media.setUploadedBy(uploadedBy);
        media.setUploadedAt(new Date());
        return mediaRepository.save(media);
    }
    
    public List<Media> getMedia() {
        return mediaRepository.findAll();
    }
    
    public List<Media> getMediaByFamilyId(Long familyId) {
        return mediaRepository.findByFamilyId(familyId);
    }
    
    public List<Media> getMediaByFamilyIdAndType(Long familyId, String type) {
        return mediaRepository.findByFamilyIdAndType(familyId, type);
    }
    
    public Media getMediaById(Long mediaId) {
        return mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));
    }
    
    public void deleteMedia(Long mediaId) {
        mediaRepository.deleteById(mediaId);
    }
    
    private String getFileType(String fileName) {
        if (fileName == null) return "unknown";
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
                return "photo";
            case "mp4":
            case "avi":
            case "mov":
                return "video";
            case "pdf":
            case "doc":
            case "docx":
            case "txt":
                return "document";
            default:
                return "unknown";
        }
    }
}
