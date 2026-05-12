package com.bage.study.ai.best.practice.dev.flow.service.impl;

import com.bage.study.ai.best.practice.dev.flow.dto.request.HistoricalEventRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.HistoricalEventDTO;
import com.bage.study.ai.best.practice.dev.flow.entity.HistoricalEvent;
import com.bage.study.ai.best.practice.dev.flow.exception.ResourceNotFoundException;
import com.bage.study.ai.best.practice.dev.flow.repository.FamilyRepository;
import com.bage.study.ai.best.practice.dev.flow.repository.HistoricalEventRepository;
import com.bage.study.ai.best.practice.dev.flow.service.HistoricalEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoricalEventServiceImpl implements HistoricalEventService {

    private final HistoricalEventRepository eventRepository;
    private final FamilyRepository familyRepository;

    @Override
    public HistoricalEventDTO createEvent(HistoricalEventRequest request) {
        log.info("创建历史事件: familyId={}, name={}", request.getFamilyId(), request.getName());

        if (!familyRepository.existsById(request.getFamilyId())) {
            throw new ResourceNotFoundException("家族不存在");
        }

        HistoricalEvent event = new HistoricalEvent();
        event.setFamilyId(request.getFamilyId());
        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setRelatedMemberIds(request.getRelatedMemberIds());
        event.setPhoto(request.getPhoto());

        HistoricalEvent savedEvent = eventRepository.save(event);
        log.info("历史事件创建成功: eventId={}", savedEvent.getId());

        return convertToDTO(savedEvent);
    }

    @Override
    public HistoricalEventDTO updateEvent(Long eventId, HistoricalEventRequest request) {
        log.info("更新历史事件: eventId={}", eventId);

        HistoricalEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("历史事件不存在"));

        if (request.getName() != null) {
            event.setName(request.getName());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getRelatedMemberIds() != null) {
            event.setRelatedMemberIds(request.getRelatedMemberIds());
        }
        if (request.getPhoto() != null) {
            event.setPhoto(request.getPhoto());
        }

        HistoricalEvent updatedEvent = eventRepository.save(event);
        log.info("历史事件更新成功: eventId={}", updatedEvent.getId());

        return convertToDTO(updatedEvent);
    }

    @Override
    public void deleteEvent(Long eventId) {
        log.info("删除历史事件: eventId={}", eventId);

        HistoricalEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("历史事件不存在"));

        event.setDeleted(1);
        eventRepository.save(event);
        log.info("历史事件删除成功: eventId={}", eventId);
    }

    @Override
    public HistoricalEventDTO getEventById(Long eventId) {
        HistoricalEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("历史事件不存在"));
        return convertToDTO(event);
    }

    @Override
    public List<HistoricalEventDTO> getEventsByFamily(Long familyId) {
        return eventRepository.findByFamilyId(familyId).stream()
                .filter(e -> e.getDeleted() == 0)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private HistoricalEventDTO convertToDTO(HistoricalEvent event) {
        HistoricalEventDTO dto = new HistoricalEventDTO();
        dto.setId(event.getId());
        dto.setFamilyId(event.getFamilyId());
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setRelatedMemberIds(event.getRelatedMemberIds());
        dto.setPhoto(event.getPhoto());
        dto.setCreatedAt(event.getCreatedAt());
        return dto;
    }
}