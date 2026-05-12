package com.bage.study.ai.best.practice.dev.flow.service.impl;

import com.bage.study.ai.best.practice.dev.flow.dto.TrackPointDTO;
import com.bage.study.ai.best.practice.dev.flow.dto.TrackPointRequest;
import com.bage.study.ai.best.practice.dev.flow.entity.TrackPoint;
import com.bage.study.ai.best.practice.dev.flow.exception.TrackPointNotFoundException;
import com.bage.study.ai.best.practice.dev.flow.repository.TrackPointRepository;
import com.bage.study.ai.best.practice.dev.flow.service.TrackPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TrackPointServiceImpl implements TrackPointService {

    private static final Logger log = LoggerFactory.getLogger(TrackPointServiceImpl.class);

    private final TrackPointRepository trackPointRepository;

    public TrackPointServiceImpl(TrackPointRepository trackPointRepository) {
        this.trackPointRepository = trackPointRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackPointDTO> getTrackPointsByUserId(Long userId) {
        log.debug("获取用户轨迹点列表: userId={}", userId);
        List<TrackPoint> points = trackPointRepository.findByUserIdOrderByCreatedAtAsc(userId);
        log.debug("获取轨迹点列表完成: userId={}, count={}", userId, points.size());
        return points.stream()
                .map(TrackPointDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TrackPointDTO getTrackPointById(Long id) {
        log.debug("获取轨迹点: id={}", id);
        TrackPoint trackPoint = trackPointRepository.findById(id)
                .orElseThrow(() -> new TrackPointNotFoundException("轨迹点不存在: " + id));
        log.debug("获取轨迹点成功: id={}, userId={}", id, trackPoint.getUserId());
        return TrackPointDTO.fromEntity(trackPoint);
    }

    @Override
    public TrackPointDTO createTrackPoint(Long userId, TrackPointRequest request) {
        log.debug("创建轨迹点: userId={}, latitude={}, longitude={}", 
                userId, request.getLatitude(), request.getLongitude());

        validateCoordinates(request.getLatitude(), request.getLongitude());

        TrackPoint trackPoint = TrackPoint.builder()
                .userId(userId)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .name(request.getName())
                .description(request.getDescription())
                .build();

        TrackPoint saved = trackPointRepository.save(trackPoint);
        log.info("创建轨迹点成功: id={}, userId={}", saved.getId(), userId);
        return TrackPointDTO.fromEntity(saved);
    }

    @Override
    public TrackPointDTO updateTrackPoint(Long id, TrackPointRequest request) {
        log.debug("更新轨迹点: id={}", id);
        TrackPoint trackPoint = trackPointRepository.findById(id)
                .orElseThrow(() -> new TrackPointNotFoundException("轨迹点不存在: " + id));

        if (request.getLatitude() != null && request.getLongitude() != null) {
            validateCoordinates(request.getLatitude(), request.getLongitude());
            trackPoint.setLatitude(request.getLatitude());
            trackPoint.setLongitude(request.getLongitude());
        }

        if (request.getName() != null) {
            trackPoint.setName(request.getName());
        }

        if (request.getDescription() != null) {
            trackPoint.setDescription(request.getDescription());
        }

        TrackPoint updated = trackPointRepository.save(trackPoint);
        log.info("更新轨迹点成功: id={}", id);
        return TrackPointDTO.fromEntity(updated);
    }

    @Override
    public void deleteTrackPoint(Long id) {
        log.debug("删除轨迹点: id={}", id);
        if (!trackPointRepository.existsById(id)) {
            throw new TrackPointNotFoundException("轨迹点不存在: " + id);
        }
        trackPointRepository.deleteById(id);
        log.info("删除轨迹点成功: id={}", id);
    }

    private void validateCoordinates(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            throw new IllegalArgumentException("纬度和经度不能为空");
        }
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("纬度必须在 -90 到 90 之间");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("经度必须在 -180 到 180 之间");
        }
    }
}