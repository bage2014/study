package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.Trajectory;
import com.bage.my.app.end.point.entity.ApiResponse;
import com.bage.my.app.end.point.service.MapTrajectoryService;
import com.bage.my.app.end.point.model.response.TrajectoryListResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/trajectorys")
public class MapTrajectoryController {
    @Autowired
    private MapTrajectoryService mapTrajectoryService;

    @PostConstruct
    public void init() {
        // 初始化模拟数据
        mapTrajectoryService.initMockData();
    }

    @RequestMapping(value = "/query")
    public ApiResponse<TrajectoryListResponse> getTrajectoryData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "time"));
            LocalDateTime start = startTime != null ? LocalDateTime.parse(startTime) : null;
            LocalDateTime end = endTime != null ? LocalDateTime.parse(endTime) : null;
            
            Page<Trajectory> trajectoryPage = mapTrajectoryService.findTrajectoriesByTimeRange(start, end, pageable);
            
            // 使用对象返回而不是Map
            TrajectoryListResponse response = new TrajectoryListResponse(
                trajectoryPage.getContent(),
                trajectoryPage.getTotalElements(),
                trajectoryPage.getTotalPages(),
                trajectoryPage.getNumber(),
                trajectoryPage.getSize()
            );
            
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.fail(400, "时间格式不正确，请使用 ISO-8601 格式: yyyy-MM-ddTHH:mm:ss");
        }
    }

    @PostMapping("/save")
    public ApiResponse<Trajectory> saveTrajectory(@RequestBody Trajectory trajectory) {
        try {
            Trajectory saved = mapTrajectoryService.saveTrajectory(trajectory);
            return ApiResponse.success(saved);
        } catch (Exception e) {
            return ApiResponse.fail(500, "保存轨迹失败: " + e.getMessage());
        }
    }
}