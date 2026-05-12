package com.bage.study.ai.best.practice.dev.flow.controller;

import com.bage.study.ai.best.practice.dev.flow.dto.TrackPointDTO;
import com.bage.study.ai.best.practice.dev.flow.dto.TrackPointRequest;
import com.bage.study.ai.best.practice.dev.flow.service.TrackPointService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trackpoints")
@CrossOrigin(origins = "*")
public class TrackPointController {

    private static final Logger log = LoggerFactory.getLogger(TrackPointController.class);

    private final TrackPointService trackPointService;
    private final MeterRegistry meterRegistry;

    public TrackPointController(TrackPointService trackPointService, MeterRegistry meterRegistry) {
        this.trackPointService = trackPointService;
        this.meterRegistry = meterRegistry;
    }

    @GetMapping
    @Timed(value = "track.point.get.by.user", description = "获取用户轨迹点列表耗时")
    public ResponseEntity<List<TrackPointDTO>> getTrackPoints(
            @RequestParam(value = "userId", defaultValue = "1") Long userId,
            @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        
        String currentTraceId = initTraceId(traceId);
        long startTime = System.currentTimeMillis();
        log.info("获取轨迹点列表: traceId={}, userId={}", currentTraceId, userId);

        try {
            List<TrackPointDTO> points = trackPointService.getTrackPointsByUserId(userId);
            long duration = System.currentTimeMillis() - startTime;
            log.info("获取轨迹点列表成功: traceId={}, userId={}, count={}, duration={}ms", 
                    currentTraceId, userId, points.size(), duration);
            return ResponseEntity.ok(points);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("获取轨迹点列表失败: traceId={}, userId={}, duration={}ms, error={}", 
                    currentTraceId, userId, duration, e.getMessage());
            throw e;
        } finally {
            MDC.remove("traceId");
        }
    }

    @GetMapping("/{id}")
    @Timed(value = "track.point.get.by.id", description = "根据ID获取轨迹点耗时")
    public ResponseEntity<TrackPointDTO> getTrackPointById(
            @PathVariable Long id,
            @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        
        String currentTraceId = initTraceId(traceId);
        long startTime = System.currentTimeMillis();
        log.info("获取轨迹点: traceId={}, id={}", currentTraceId, id);

        try {
            TrackPointDTO point = trackPointService.getTrackPointById(id);
            long duration = System.currentTimeMillis() - startTime;
            log.info("获取轨迹点成功: traceId={}, id={}, duration={}ms", currentTraceId, id, duration);
            return ResponseEntity.ok(point);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("获取轨迹点失败: traceId={}, id={}, duration={}ms, error={}", 
                    currentTraceId, id, duration, e.getMessage());
            throw e;
        } finally {
            MDC.remove("traceId");
        }
    }

    @PostMapping
    @Timed(value = "track.point.create", description = "创建轨迹点耗时")
    public ResponseEntity<TrackPointDTO> createTrackPoint(
            @Valid @RequestBody TrackPointRequest request,
            @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        
        String currentTraceId = initTraceId(traceId);
        long startTime = System.currentTimeMillis();
        log.info("创建轨迹点: traceId={}, latitude={}, longitude={}, name={}", 
                currentTraceId, request.getLatitude(), request.getLongitude(), request.getName());

        try {
            TrackPointDTO created = trackPointService.createTrackPoint(1L, request);
            long duration = System.currentTimeMillis() - startTime;
            log.info("创建轨迹点成功: traceId={}, id={}, duration={}ms", currentTraceId, created.getId(), duration);
            meterRegistry.counter("track.point.create.success").increment();
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("创建轨迹点失败: traceId={}, latitude={}, longitude={}, duration={}ms, error={}", 
                    currentTraceId, request.getLatitude(), request.getLongitude(), duration, e.getMessage());
            meterRegistry.counter("track.point.create.failure").increment();
            throw e;
        } finally {
            MDC.remove("traceId");
        }
    }

    @PutMapping("/{id}")
    @Timed(value = "track.point.update", description = "更新轨迹点耗时")
    public ResponseEntity<TrackPointDTO> updateTrackPoint(
            @PathVariable Long id,
            @Valid @RequestBody TrackPointRequest request,
            @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        
        String currentTraceId = initTraceId(traceId);
        long startTime = System.currentTimeMillis();
        log.info("更新轨迹点: traceId={}, id={}", currentTraceId, id);

        try {
            TrackPointDTO updated = trackPointService.updateTrackPoint(id, request);
            long duration = System.currentTimeMillis() - startTime;
            log.info("更新轨迹点成功: traceId={}, id={}, duration={}ms", currentTraceId, id, duration);
            meterRegistry.counter("track.point.update.success").increment();
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("更新轨迹点失败: traceId={}, id={}, duration={}ms, error={}", 
                    currentTraceId, id, duration, e.getMessage());
            meterRegistry.counter("track.point.update.failure").increment();
            throw e;
        } finally {
            MDC.remove("traceId");
        }
    }

    @DeleteMapping("/{id}")
    @Timed(value = "track.point.delete", description = "删除轨迹点耗时")
    public ResponseEntity<Void> deleteTrackPoint(
            @PathVariable Long id,
            @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        
        String currentTraceId = initTraceId(traceId);
        long startTime = System.currentTimeMillis();
        log.info("删除轨迹点: traceId={}, id={}", currentTraceId, id);

        try {
            trackPointService.deleteTrackPoint(id);
            long duration = System.currentTimeMillis() - startTime;
            log.info("删除轨迹点成功: traceId={}, id={}, duration={}ms", currentTraceId, id, duration);
            meterRegistry.counter("track.point.delete.success").increment();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("删除轨迹点失败: traceId={}, id={}, duration={}ms, error={}", 
                    currentTraceId, id, duration, e.getMessage());
            meterRegistry.counter("track.point.delete.failure").increment();
            throw e;
        } finally {
            MDC.remove("traceId");
        }
    }

    private String initTraceId(String traceId) {
        String currentTraceId = traceId != null ? traceId : UUID.randomUUID().toString();
        MDC.put("traceId", currentTraceId);
        return currentTraceId;
    }
}