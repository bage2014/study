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
    
    public Event addEvent(Long familyId, String name, String description, Date eventDate, String relatedMembers, String photo) {
        Event event = new Event();
        event.setFamilyId(familyId);
        event.setName(name);
        event.setDescription(description);
        event.setEventDate(eventDate);
        event.setRelatedMembers(relatedMembers);
        event.setPhoto(photo);
        event.setCreatedAt(new Date());
        return eventRepository.save(event);
    }
    
    public List<Event> getEventsByFamilyId(Long familyId) {
        return eventRepository.findByFamilyId(familyId);
    }
    
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }
    
    public Event updateEvent(Long eventId, String name, String description, Date eventDate, String relatedMembers, String photo) {
        Event event = getEventById(eventId);
        if (name != null) event.setName(name);
        if (description != null) event.setDescription(description);
        if (eventDate != null) event.setEventDate(eventDate);
        if (relatedMembers != null) event.setRelatedMembers(relatedMembers);
        if (photo != null) event.setPhoto(photo);
        return eventRepository.save(event);
    }
    
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }
}