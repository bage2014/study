package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Media;
import com.familytree.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/media")
public class MediaController {
    @Autowired
    private MediaService mediaService;
    
    @PostMapping
    public ApiResponse<Media> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam("familyId") Long familyId,
            @RequestParam("description") String description) {
        try {
            Media uploadedMedia = mediaService.uploadMedia(file, familyId, description);
            return ApiResponse.success(uploadedMedia);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping
    public ApiResponse<List<Media>> getMedia() {
        try {
            List<Media> mediaList = mediaService.getMedia();
            return ApiResponse.success(mediaList);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Media> getMedia(@PathVariable Long id) {
        try {
            Media media = mediaService.getMediaById(id);
            return ApiResponse.success(media);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMedia(@PathVariable Long id) {
        try {
            mediaService.deleteMedia(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
