package com.familytree.controller;

import com.familytree.dto.ApiResponse;
import com.familytree.model.Event;
import com.familytree.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {
    @Mock
    private EventService eventService;
    
    @InjectMocks
    private EventController eventController;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testCreateEventSuccess() {
        // Arrange
        Event event = new Event();
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setDate(new Date());
        event.setLocation("Test Location");
        event.setFamilyId(1L);
        
        Event createdEvent = new Event();
        createdEvent.setId(1L);
        createdEvent.setTitle("Test Event");
        createdEvent.setDescription("Test Description");
        createdEvent.setDate(new Date());
        createdEvent.setLocation("Test Location");
        createdEvent.setFamilyId(1L);
        
        when(eventService.createEvent(
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getLocation(),
                event.getFamilyId()
        )).thenReturn(createdEvent);
        
        // Act
        ApiResponse<Event> response = eventController.createEvent(event);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(createdEvent, response.getData());
        verify(eventService, times(1)).createEvent(
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getLocation(),
                event.getFamilyId()
        );
    }
    
    @Test
    void testCreateEventFailure() {
        // Arrange
        Event event = new Event();
        event.setTitle("Test Event");
        event.setDescription("Test Description");
        event.setDate(new Date());
        event.setLocation("Test Location");
        event.setFamilyId(1L);
        
        String errorMessage = "Failed to create event";
        when(eventService.createEvent(
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getLocation(),
                event.getFamilyId()
        )).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Event> response = eventController.createEvent(event);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(eventService, times(1)).createEvent(
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getLocation(),
                event.getFamilyId()
        );
    }
    
    @Test
    void testGetEventsSuccess() {
        // Arrange
        List<Event> events = new ArrayList<>();
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");
        events.add(event1);
        
        Event event2 = new Event();
        event2.setId(2L);
        event2.setTitle("Event 2");
        events.add(event2);
        
        when(eventService.getEvents()).thenReturn(events);
        
        // Act
        ApiResponse<List<Event>> response = eventController.getEvents();
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(events, response.getData());
        verify(eventService, times(1)).getEvents();
    }
    
    @Test
    void testGetEventsFailure() {
        // Arrange
        String errorMessage = "Failed to get events";
        when(eventService.getEvents()).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<List<Event>> response = eventController.getEvents();
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(eventService, times(1)).getEvents();
    }
    
    @Test
    void testGetEventSuccess() {
        // Arrange
        Long eventId = 1L;
        Event event = new Event();
        event.setId(eventId);
        event.setTitle("Test Event");
        
        when(eventService.getEventById(eventId)).thenReturn(event);
        
        // Act
        ApiResponse<Event> response = eventController.getEvent(eventId);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(event, response.getData());
        verify(eventService, times(1)).getEventById(eventId);
    }
    
    @Test
    void testGetEventFailure() {
        // Arrange
        Long eventId = 1L;
        String errorMessage = "Event not found";
        when(eventService.getEventById(eventId)).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Event> response = eventController.getEvent(eventId);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(eventService, times(1)).getEventById(eventId);
    }
    
    @Test
    void testUpdateEventSuccess() {
        // Arrange
        Long eventId = 1L;
        Event event = new Event();
        event.setTitle("Updated Event");
        event.setDescription("Updated Description");
        event.setDate(new Date());
        event.setLocation("Updated Location");
        event.setFamilyId(1L);
        
        Event updatedEvent = new Event();
        updatedEvent.setId(eventId);
        updatedEvent.setTitle("Updated Event");
        updatedEvent.setDescription("Updated Description");
        updatedEvent.setDate(new Date());
        updatedEvent.setLocation("Updated Location");
        updatedEvent.setFamilyId(1L);
        
        when(eventService.updateEvent(
                eventId,
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getLocation(),
                event.getFamilyId()
        )).thenReturn(updatedEvent);
        
        // Act
        ApiResponse<Event> response = eventController.updateEvent(eventId, event);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(updatedEvent, response.getData());
        verify(eventService, times(1)).updateEvent(
                eventId,
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getLocation(),
                event.getFamilyId()
        );
    }
    
    @Test
    void testUpdateEventFailure() {
        // Arrange
        Long eventId = 1L;
        Event event = new Event();
        event.setTitle("Updated Event");
        event.setDescription("Updated Description");
        event.setDate(new Date());
        event.setLocation("Updated Location");
        event.setFamilyId(1L);
        
        String errorMessage = "Failed to update event";
        when(eventService.updateEvent(
                eventId,
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getLocation(),
                event.getFamilyId()
        )).thenThrow(new RuntimeException(errorMessage));
        
        // Act
        ApiResponse<Event> response = eventController.updateEvent(eventId, event);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(eventService, times(1)).updateEvent(
                eventId,
                event.getTitle(),
                event.getDescription(),
                event.getDate(),
                event.getLocation(),
                event.getFamilyId()
        );
    }
    
    @Test
    void testDeleteEventSuccess() {
        // Arrange
        Long eventId = 1L;
        doNothing().when(eventService).deleteEvent(eventId);
        
        // Act
        ApiResponse<Void> response = eventController.deleteEvent(eventId);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertNull(response.getData());
        verify(eventService, times(1)).deleteEvent(eventId);
    }
    
    @Test
    void testDeleteEventFailure() {
        // Arrange
        Long eventId = 1L;
        String errorMessage = "Failed to delete event";
        doThrow(new RuntimeException(errorMessage)).when(eventService).deleteEvent(eventId);
        
        // Act
        ApiResponse<Void> response = eventController.deleteEvent(eventId);
        
        // Assert
        assertNotNull(response);
        assertEquals(400, response.getCode());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getData());
        verify(eventService, times(1)).deleteEvent(eventId);
    }
}