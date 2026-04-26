package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Media;
import com.familytree.service.MediaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MediaControllerTest {
    @Mock
    private MediaService mediaService;
    
    @Mock
    private MultipartFile file;
    
    @InjectMocks
    private MediaController mediaController;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testUploadMediaSuccess() {
        // Arrange
        Long familyId = 1L;
        String description = "Test Media";
        
        Media uploadedMedia = new Media();
        uploadedMedia.setId(1L);
        uploadedMedia.setTitle("Test Media");
        uploadedMedia.setDescription(description);
        uploadedMedia.setFamilyId(familyId);
        
        when(mediaService.uploadMedia(file, familyId, description)).thenReturn(uploadedMedia);
        
        // Act
        ApiResponse<Media> response = mediaController.uploadMedia(file, familyId, description);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNull(response.getMessage());
        assertNotNull(response.getData());
        assertEquals(uploadedMedia, response.getData());
        verify(mediaService, times(1)).uploadMedia(file, familyId, description);
    }
    
    @Test
    void testUploadMediaFailure() {
        // Arrange
        Long familyId = 1L;
        String description = "Test Media";
        
        String errorMessage = "Failed to upload media";
        when(mediaService.uploadMedia(file, familyId, description)).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Media> response = mediaController.uploadMedia(file, familyId, description);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(mediaService, times(1)).uploadMedia(file, familyId, description);
    }
    
    @Test
    void testGetMediaSuccess() {
        // Arrange
        List<Media> mediaList = new ArrayList<>();
        Media media1 = new Media();
        media1.setId(1L);
        media1.setTitle("Media 1");
        mediaList.add(media1);
        
        Media media2 = new Media();
        media2.setId(2L);
        media2.setTitle("Media 2");
        mediaList.add(media2);
        
        when(mediaService.getMedia()).thenReturn(mediaList);
        
        // Act
        ApiResponse<List<Media>> response = mediaController.getMedia();
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNull(response.getMessage());
        assertNotNull(response.getData());
        assertEquals(mediaList, response.getData());
        verify(mediaService, times(1)).getMedia();
    }
    
    @Test
    void testGetMediaFailure() {
        // Arrange
        String errorMessage = "Failed to get media";
        when(mediaService.getMedia()).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<List<Media>> response = mediaController.getMedia();
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(mediaService, times(1)).getMedia();
    }
    
    @Test
    void testGetMediaByIdSuccess() {
        // Arrange
        Long mediaId = 1L;
        Media media = new Media();
        media.setId(mediaId);
        media.setTitle("Test Media");
        
        when(mediaService.getMediaById(mediaId)).thenReturn(media);
        
        // Act
        ApiResponse<Media> response = mediaController.getMedia(mediaId);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNull(response.getMessage());
        assertNotNull(response.getData());
        assertEquals(media, response.getData());
        verify(mediaService, times(1)).getMediaById(mediaId);
    }
    
    @Test
    void testGetMediaByIdFailure() {
        // Arrange
        Long mediaId = 1L;
        String errorMessage = "Media not found";
        when(mediaService.getMediaById(mediaId)).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Media> response = mediaController.getMedia(mediaId);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(mediaService, times(1)).getMediaById(mediaId);
    }
    
    @Test
    void testDeleteMediaSuccess() {
        // Arrange
        Long mediaId = 1L;
        doNothing().when(mediaService).deleteMedia(mediaId);
        
        // Act
        ApiResponse<Void> response = mediaController.deleteMedia(mediaId);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNull(response.getMessage());
        assertNull(response.getData());
        verify(mediaService, times(1)).deleteMedia(mediaId);
    }
    
    @Test
    void testDeleteMediaFailure() {
        // Arrange
        Long mediaId = 1L;
        String errorMessage = "Failed to delete media";
        doThrow(new RuntimeException(errorMessage)).when(mediaService).deleteMedia(mediaId);
        
        // Act
        ApiResponse<Void> response = mediaController.deleteMedia(mediaId);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(mediaService, times(1)).deleteMedia(mediaId);
    }
}