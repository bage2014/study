package com.bage.study.ai.best.practice.dev.flow.service;

import com.bage.study.ai.best.practice.dev.flow.dto.request.HistoricalEventRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.HistoricalEventDTO;

import java.util.List;

public interface HistoricalEventService {

    HistoricalEventDTO createEvent(HistoricalEventRequest request);

    HistoricalEventDTO updateEvent(Long eventId, HistoricalEventRequest request);

    void deleteEvent(Long eventId);

    HistoricalEventDTO getEventById(Long eventId);

    List<HistoricalEventDTO> getEventsByFamily(Long familyId);
}