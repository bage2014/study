package com.bage.study.ai.best.practice.dev.flow.controller;

import com.bage.study.ai.best.practice.dev.flow.dto.TrackPointDTO;
import com.bage.study.ai.best.practice.dev.flow.dto.TrackPointRequest;
import com.bage.study.ai.best.practice.dev.flow.service.TrackPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trackpoints")
public class TrackPointController {

    private static final Logger log = LoggerFactory.getLogger(TrackPointController.class);

    private final TrackPointService trackPointService;

    @Autowired
    public TrackPointController(TrackPointService trackPointService) {
        this.trackPointService = trackPointService;
    }

    @GetMapping
    public ResponseEntity<List<TrackPointDTO>> getTrackPoints(
            @RequestParam(value = "userId", defaultValue = "1") Long userId) {
        long startTime = System.currentTimeMillis();
        log.info("获取轨迹点列表: userId={}", userId);

        List<TrackPointDTO> points = trackPointService.getTrackPointsByUserId(userId);
        long duration = System.currentTimeMillis() - startTime;
        log.info("获取轨迹点列表成功: userId={}, count={}, duration={}ms", userId, points.size(), duration);

        return ResponseEntity.ok(points);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackPointDTO> getTrackPointById(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        log.info("获取轨迹点: id={}", id);

        TrackPointDTO point = trackPointService.getTrackPointById(id);
        long duration = System.currentTimeMillis() - startTime;
        log.info("获取轨迹点成功: id={}, duration={}ms", id, duration);

        return ResponseEntity.ok(point);
    }

    @PostMapping
    public ResponseEntity<TrackPointDTO> createTrackPoint(@RequestBody TrackPointRequest request) {
        long startTime = System.currentTimeMillis();
        log.info("创建轨迹点: latitude={}, longitude={}", request.getLatitude(), request.getLongitude());

        TrackPointDTO created = trackPointService.createTrackPoint(1L, request);
        long duration = System.currentTimeMillis() - startTime;
        log.info("创建轨迹点成功: id={}, duration={}ms", created.getId(), duration);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrackPointDTO> updateTrackPoint(
            @PathVariable Long id,
            @RequestBody TrackPointRequest request) {
        long startTime = System.currentTimeMillis();
        log.info("更新轨迹点: id={}", id);

        TrackPointDTO updated = trackPointService.updateTrackPoint(id, request);
        long duration = System.currentTimeMillis() - startTime;
        log.info("更新轨迹点成功: id={}, duration={}ms", id, duration);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrackPoint(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        log.info("删除轨迹点: id={}", id);

        trackPointService.deleteTrackPoint(id);
        long duration = System.currentTimeMillis() - startTime;
        log.info("删除轨迹点成功: id={}, duration={}ms", id, duration);

        return ResponseEntity.noContent().build();
    }
}