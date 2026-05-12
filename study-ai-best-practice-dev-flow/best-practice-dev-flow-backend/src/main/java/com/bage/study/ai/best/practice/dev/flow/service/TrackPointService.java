package com.bage.study.ai.best.practice.dev.flow.service;

import com.bage.study.ai.best.practice.dev.flow.dto.TrackPointDTO;
import com.bage.study.ai.best.practice.dev.flow.dto.TrackPointRequest;

import java.util.List;

public interface TrackPointService {

    List<TrackPointDTO> getTrackPointsByUserId(Long userId);

    TrackPointDTO getTrackPointById(Long id);

    TrackPointDTO createTrackPoint(Long userId, TrackPointRequest request);

    TrackPointDTO updateTrackPoint(Long id, TrackPointRequest request);

    void deleteTrackPoint(Long id);
}