package com.familytree.controller;

import com.familytree.model.Media;
import com.familytree.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/families/{familyId}/media")
public class MediaController {
    @Autowired
    private MediaService mediaService;
    
    @PostMapping
    public ResponseEntity<Media> uploadMedia(@PathVariable Long familyId, @RequestAttribute("userId") Long userId, @RequestBody Media media) {
        Media uploadedMedia = mediaService.uploadMedia(
                familyId,
                media.getType(),
                media.getUrl(),
                media.getDescription(),
                userId
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadedMedia);
    }
    
    @GetMapping
    public ResponseEntity<List<Media>> getMedia(@PathVariable Long familyId) {
        List<Media> mediaList = mediaService.getMediaByFamilyId(familyId);
        return ResponseEntity.ok(mediaList);
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Media>> getMediaByType(@PathVariable Long familyId, @PathVariable String type) {
        List<Media> mediaList = mediaService.getMediaByFamilyIdAndType(familyId, type);
        return ResponseEntity.ok(mediaList);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.noContent().build();
    }
}