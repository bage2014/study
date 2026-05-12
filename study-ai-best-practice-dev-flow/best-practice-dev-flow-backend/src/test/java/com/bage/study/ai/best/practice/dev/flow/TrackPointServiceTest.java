package com.bage.study.ai.best.practice.dev.flow;

import com.bage.study.ai.best.practice.dev.flow.dto.TrackPointDTO;
import com.bage.study.ai.best.practice.dev.flow.dto.TrackPointRequest;
import com.bage.study.ai.best.practice.dev.flow.entity.TrackPoint;
import com.bage.study.ai.best.practice.dev.flow.exception.TrackPointNotFoundException;
import com.bage.study.ai.best.practice.dev.flow.repository.TrackPointRepository;
import com.bage.study.ai.best.practice.dev.flow.service.impl.TrackPointServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TrackPointServiceTest {

    @Mock
    private TrackPointRepository trackPointRepository;

    @InjectMocks
    private TrackPointServiceImpl trackPointService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTrackPointsByUserId_Success() {
        TrackPoint point1 = TrackPoint.builder()
                .id(1L)
                .userId(1L)
                .latitude(39.9042)
                .longitude(116.4074)
                .name("天安门")
                .createdAt(LocalDateTime.now())
                .build();

        TrackPoint point2 = TrackPoint.builder()
                .id(2L)
                .userId(1L)
                .latitude(39.9142)
                .longitude(116.4174)
                .name("故宫")
                .createdAt(LocalDateTime.now().plusMinutes(30))
                .build();

        when(trackPointRepository.findByUserIdOrderByCreatedAtAsc(1L))
                .thenReturn(Arrays.asList(point1, point2));

        List<TrackPointDTO> result = trackPointService.getTrackPointsByUserId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("天安门", result.get(0).getName());
        assertEquals("故宫", result.get(1).getName());
    }

    @Test
    void testGetTrackPointById_Success() {
        TrackPoint point = TrackPoint.builder()
                .id(1L)
                .userId(1L)
                .latitude(39.9042)
                .longitude(116.4074)
                .name("天安门")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(trackPointRepository.findById(1L)).thenReturn(Optional.of(point));

        TrackPointDTO result = trackPointService.getTrackPointById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("天安门", result.getName());
    }

    @Test
    void testGetTrackPointById_NotFound() {
        when(trackPointRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(TrackPointNotFoundException.class, () -> trackPointService.getTrackPointById(999L));
    }

    @Test
    void testCreateTrackPoint_Success() {
        TrackPointRequest request = new TrackPointRequest();
        request.setLatitude(39.9042);
        request.setLongitude(116.4074);
        request.setName("天安门");
        request.setDescription("首都北京标志性建筑");

        when(trackPointRepository.save(any(TrackPoint.class))).thenAnswer(invocation -> {
            TrackPoint point = invocation.getArgument(0);
            point.setId(1L);
            return point;
        });

        TrackPointDTO result = trackPointService.createTrackPoint(1L, request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("天安门", result.getName());
        assertEquals("首都北京标志性建筑", result.getDescription());
        verify(trackPointRepository, times(1)).save(any(TrackPoint.class));
    }

    @Test
    void testCreateTrackPoint_NullCoordinates() {
        TrackPointRequest request = new TrackPointRequest();
        request.setLatitude(null);
        request.setLongitude(null);

        assertThrows(IllegalArgumentException.class, () -> trackPointService.createTrackPoint(1L, request));
        verify(trackPointRepository, never()).save(any(TrackPoint.class));
    }

    @Test
    void testCreateTrackPoint_InvalidLatitude() {
        TrackPointRequest request = new TrackPointRequest();
        request.setLatitude(91.0);
        request.setLongitude(116.4074);

        assertThrows(IllegalArgumentException.class, () -> trackPointService.createTrackPoint(1L, request));
    }

    @Test
    void testCreateTrackPoint_InvalidLongitude() {
        TrackPointRequest request = new TrackPointRequest();
        request.setLatitude(39.9042);
        request.setLongitude(181.0);

        assertThrows(IllegalArgumentException.class, () -> trackPointService.createTrackPoint(1L, request));
    }

    @Test
    void testUpdateTrackPoint_Success() {
        TrackPoint existing = TrackPoint.builder()
                .id(1L)
                .userId(1L)
                .latitude(39.9042)
                .longitude(116.4074)
                .name("旧名称")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TrackPointRequest request = new TrackPointRequest();
        request.setName("新名称");

        when(trackPointRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(trackPointRepository.save(any(TrackPoint.class))).thenReturn(existing);

        TrackPointDTO result = trackPointService.updateTrackPoint(1L, request);

        assertNotNull(result);
        assertEquals("新名称", result.getName());
    }

    @Test
    void testUpdateTrackPoint_NotFound() {
        TrackPointRequest request = new TrackPointRequest();
        request.setName("新名称");

        when(trackPointRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(TrackPointNotFoundException.class, () -> trackPointService.updateTrackPoint(999L, request));
    }

    @Test
    void testDeleteTrackPoint_Success() {
        when(trackPointRepository.existsById(1L)).thenReturn(true);
        doNothing().when(trackPointRepository).deleteById(1L);

        trackPointService.deleteTrackPoint(1L);

        verify(trackPointRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTrackPoint_NotFound() {
        when(trackPointRepository.existsById(999L)).thenReturn(false);

        assertThrows(TrackPointNotFoundException.class, () -> trackPointService.deleteTrackPoint(999L));
    }
}