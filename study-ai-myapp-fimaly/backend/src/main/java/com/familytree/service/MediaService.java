package com.familytree.service;

import com.familytree.model.Media;
import com.familytree.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MediaService {
    @Autowired
    private MediaRepository mediaRepository;
    
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
}