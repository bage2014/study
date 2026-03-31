package com.familytree.service;

import com.familytree.model.Event;
import com.familytree.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    
    public Event createEvent(String title, String description, Date date, String location, Long familyId) {
        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setEventDate(date);
        event.setLocation(location);
        event.setFamilyId(familyId);
        event.setCreatedAt(new Date());
        return eventRepository.save(event);
    }
    
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }
    
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }
    
    public Event updateEvent(Long id, String title, String description, Date date, String location, Long familyId) {
        Event event = getEventById(id);
        if (title != null) event.setTitle(title);
        if (description != null) event.setDescription(description);
        if (date != null) event.setEventDate(date);
        if (location != null) event.setLocation(location);
        if (familyId != null) event.setFamilyId(familyId);
        return eventRepository.save(event);
    }
    
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}