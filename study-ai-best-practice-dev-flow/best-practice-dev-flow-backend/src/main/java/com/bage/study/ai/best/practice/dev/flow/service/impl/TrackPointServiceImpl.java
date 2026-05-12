package com.bage.study.ai.best.practice.dev.flow.service.impl;

import com.bage.study.ai.best.practice.dev.flow.dto.TrackPointDTO;
import com.bage.study.ai.best.practice.dev.flow.dto.TrackPointRequest;
import com.bage.study.ai.best.practice.dev.flow.entity.TrackPoint;
import com.bage.study.ai.best.practice.dev.flow.repository.TrackPointRepository;
import com.bage.study.ai.best.practice.dev.flow.service.TrackPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrackPointServiceImpl implements TrackPointService {

    private final TrackPointRepository trackPointRepository;

    @Autowired
    public TrackPointServiceImpl(TrackPointRepository trackPointRepository) {
        this.trackPointRepository = trackPointRepository;
    }

    @Override
    public List<TrackPointDTO> getTrackPointsByUserId(Long userId) {
        List<TrackPoint> points = trackPointRepository.findByUserIdOrderByCreatedAtAsc(userId);
        return points.stream()
                .map(TrackPointDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public TrackPointDTO getTrackPointById(Long id) {
        TrackPoint trackPoint = trackPointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("轨迹点不存在: " + id));
        return TrackPointDTO.fromEntity(trackPoint);
    }

    @Override
    public TrackPointDTO createTrackPoint(Long userId, TrackPointRequest request) {
        validateCoordinates(request.getLatitude(), request.getLongitude());

        TrackPoint trackPoint = new TrackPoint();
        trackPoint.setUserId(userId);
        trackPoint.setLatitude(request.getLatitude());
        trackPoint.setLongitude(request.getLongitude());
        trackPoint.setName(request.getName());
        trackPoint.setDescription(request.getDescription());

        TrackPoint saved = trackPointRepository.save(trackPoint);
        return TrackPointDTO.fromEntity(saved);
    }

    @Override
    public TrackPointDTO updateTrackPoint(Long id, TrackPointRequest request) {
        TrackPoint trackPoint = trackPointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("轨迹点不存在: " + id));

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
        return TrackPointDTO.fromEntity(updated);
    }

    @Override
    public void deleteTrackPoint(Long id) {
        if (!trackPointRepository.existsById(id)) {
            throw new IllegalArgumentException("轨迹点不存在: " + id);
        }
        trackPointRepository.deleteById(id);
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