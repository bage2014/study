package com.bage.study.ai.best.practice.dev.flow.controller;

import com.bage.study.ai.best.practice.dev.flow.dto.RestResult;
import com.bage.study.ai.best.practice.dev.flow.dto.request.HistoricalEventRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.HistoricalEventDTO;
import com.bage.study.ai.best.practice.dev.flow.service.HistoricalEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Slf4j
public class HistoricalEventController {

    private final HistoricalEventService eventService;

    @PostMapping
    public ResponseEntity<RestResult<HistoricalEventDTO>> createEvent(@Valid @RequestBody HistoricalEventRequest request) {
        HistoricalEventDTO event = eventService.createEvent(request);
        return ResponseEntity.ok(RestResult.success("事件创建成功", event));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResult<HistoricalEventDTO>> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody HistoricalEventRequest request) {
        HistoricalEventDTO event = eventService.updateEvent(id, request);
        return ResponseEntity.ok(RestResult.success("事件更新成功", event));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResult<Void>> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(RestResult.success(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResult<HistoricalEventDTO>> getEventById(@PathVariable Long id) {
        HistoricalEventDTO event = eventService.getEventById(id);
        return ResponseEntity.ok(RestResult.success(event));
    }

    @GetMapping("/family")
    public ResponseEntity<RestResult<List<HistoricalEventDTO>>> getEventsByFamily(@RequestParam Long familyId) {
        List<HistoricalEventDTO> events = eventService.getEventsByFamily(familyId);
        return ResponseEntity.ok(RestResult.success(events));
    }
}