package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.Trajectory;
import com.bage.my.app.end.point.repository.TrajectoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class MapTrajectoryControllerTest {
    @Mock
    private TrajectoryRepository trajectoryRepository;

    @InjectMocks
    private MapTrajectoryController mapTrajectoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTrajectoryDataWithoutTimeParams() {
        // 准备测试数据
        List<Trajectory> trajectoryList = new ArrayList<>();
        Trajectory trajectory = new Trajectory(31.18, 121.37, LocalDateTime.now(), "长宁区仙霞路");
        trajectoryList.add(trajectory);
        Page<Trajectory> trajectoryPage = new PageImpl<>(trajectoryList);

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "time"));
        when(trajectoryRepository.findAll(pageable)).thenReturn(trajectoryPage);

        Page<Map<String, Object>> result = mapTrajectoryController.getTrajectoryData(0, 5, null, null);

        assertEquals(1, result.getContent().size());
        Map<String, Object> firstItem = result.getContent().get(0);
        assertEquals(31.18, firstItem.get("latitude"));
        assertEquals(121.37, firstItem.get("longitude"));
    }

    @Test
    void testGetTrajectoryDataWithTimeRange() {
        // 准备测试数据
        List<Trajectory> trajectoryList = new ArrayList<>();
        Trajectory trajectory = new Trajectory(31.18, 121.37, LocalDateTime.now(), "长宁区仙霞路");
        trajectoryList.add(trajectory);
        Page<Trajectory> trajectoryPage = new PageImpl<>(trajectoryList);

        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 31, 23, 59);
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "time"));
        when(trajectoryRepository.findByTimeBetween(start, end, pageable)).thenReturn(trajectoryPage);

        Page<Map<String, Object>> result = mapTrajectoryController.getTrajectoryData(0, 5, "2024-01-01T00:00:00", "2024-01-31T23:59:00");

        assertEquals(1, result.getContent().size());
        Map<String, Object> firstItem = result.getContent().get(0);
        assertEquals(31.18, firstItem.get("latitude"));
        assertEquals(121.37, firstItem.get("longitude"));
    }

    @Test
    void testGetTrajectoryDataWithStartTime() {
        // 准备测试数据
        List<Trajectory> trajectoryList = new ArrayList<>();
        Trajectory trajectory = new Trajectory(31.18, 121.37, LocalDateTime.now(), "长宁区仙霞路");
        trajectoryList.add(trajectory);
        Page<Trajectory> trajectoryPage = new PageImpl<>(trajectoryList);

        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "time"));
        when(trajectoryRepository.findByTimeAfter(start, pageable)).thenReturn(trajectoryPage);

        Page<Map<String, Object>> result = mapTrajectoryController.getTrajectoryData(0, 5, "2024-01-01T00:00:00", null);

        assertEquals(1, result.getContent().size());
        Map<String, Object> firstItem = result.getContent().get(0);
        assertEquals(31.18, firstItem.get("latitude"));
        assertEquals(121.37, firstItem.get("longitude"));
    }

    @Test
    void testGetTrajectoryDataWithEndTime() {
        // 准备测试数据
        List<Trajectory> trajectoryList = new ArrayList<>();
        Trajectory trajectory = new Trajectory(31.18, 121.37, LocalDateTime.now(), "长宁区仙霞路");
        trajectoryList.add(trajectory);
        Page<Trajectory> trajectoryPage = new PageImpl<>(trajectoryList);

        LocalDateTime end = LocalDateTime.of(2024, 1, 31, 23, 59);
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "time"));
        when(trajectoryRepository.findByTimeBefore(end, pageable)).thenReturn(trajectoryPage);

        Page<Map<String, Object>> result = mapTrajectoryController.getTrajectoryData(0, 5, null, "2024-01-31T23:59:00");

        assertEquals(1, result.getContent().size());
        Map<String, Object> firstItem = result.getContent().get(0);
        assertEquals(31.18, firstItem.get("latitude"));
        assertEquals(121.37, firstItem.get("longitude"));
    }

    @Test
    void testGetTrajectoryDataWithInvalidTimeFormat() {
        try {
            mapTrajectoryController.getTrajectoryData(0, 5, "2024/01/01", null);
        } catch (IllegalArgumentException e) {
            assertEquals("时间格式不正确，请使用 ISO-8601 格式: yyyy-MM-ddTHH:mm:ss", e.getMessage());
        }
    }
}