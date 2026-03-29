package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Event;
import com.familytree.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    private EventService eventService;
    
    @PostMapping
    public ApiResponse<Event> createEvent(@RequestBody Event event) {
        try {
            Event createdEvent = eventService.createEvent(
                    event.getTitle(),
                    event.getDescription(),
                    event.getDate(),
                    event.getLocation(),
                    event.getFamilyId()
            );
            return ApiResponse.success(createdEvent);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping
    public ApiResponse<List<Event>> getEvents() {
        try {
            List<Event> events = eventService.getEvents();
            return ApiResponse.success(events);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Event> getEvent(@PathVariable Long id) {
        try {
            Event event = eventService.getEventById(id);
            return ApiResponse.success(event);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        try {
            Event updatedEvent = eventService.updateEvent(
                    id,
                    event.getTitle(),
                    event.getDescription(),
                    event.getDate(),
                    event.getLocation(),
                    event.getFamilyId()
            );
            return ApiResponse.success(updatedEvent);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
