package com.familytree.controller;

import com.familytree.model.Event;
import com.familytree.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/families/{familyId}/events")
public class EventController {
    @Autowired
    private EventService eventService;
    
    @PostMapping
    public ResponseEntity<Event> addEvent(@PathVariable Long familyId, @RequestBody Event event) {
        Event createdEvent = eventService.addEvent(
                familyId,
                event.getName(),
                event.getDescription(),
                event.getEventDate(),
                event.getRelatedMembers(),
                event.getPhoto()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }
    
    @GetMapping
    public ResponseEntity<List<Event>> getEvents(@PathVariable Long familyId) {
        List<Event> events = eventService.getEventsByFamilyId(familyId);
        return ResponseEntity.ok(events);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        Event updatedEvent = eventService.updateEvent(
                id,
                event.getName(),
                event.getDescription(),
                event.getEventDate(),
                event.getRelatedMembers(),
                event.getPhoto()
        );
        return ResponseEntity.ok(updatedEvent);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}