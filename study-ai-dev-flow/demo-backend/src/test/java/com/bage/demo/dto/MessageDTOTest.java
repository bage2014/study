package com.bage.demo.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageDTOTest {

    @Test
    void testNoArgsConstructor() {
        MessageDTO dto = new MessageDTO();
        assertNull(dto.getId());
        assertNull(dto.getContent());
        assertNull(dto.getSender());
    }

    @Test
    void testAllArgsConstructor() {
        MessageDTO dto = new MessageDTO(1L, "Hello", "Alice");
        assertEquals(1L, dto.getId());
        assertEquals("Hello", dto.getContent());
        assertEquals("Alice", dto.getSender());
    }

    @Test
    void testBuilder() {
        MessageDTO dto = MessageDTO.builder()
                .id(2L)
                .content("World")
                .sender("Bob")
                .build();
        assertEquals(2L, dto.getId());
        assertEquals("World", dto.getContent());
        assertEquals("Bob", dto.getSender());
    }

    @Test
    void testSettersAndGetters() {
        MessageDTO dto = new MessageDTO();
        dto.setId(3L);
        dto.setContent("Test");
        dto.setSender("Charlie");

        assertEquals(3L, dto.getId());
        assertEquals("Test", dto.getContent());
        assertEquals("Charlie", dto.getSender());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageDTO dto1 = new MessageDTO(1L, "Hello", "Alice");
        MessageDTO dto2 = new MessageDTO(1L, "Hello", "Alice");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testNotEquals() {
        MessageDTO dto1 = new MessageDTO(1L, "Hello", "Alice");
        MessageDTO dto2 = new MessageDTO(2L, "World", "Bob");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        MessageDTO dto = new MessageDTO(1L, "Hello", "Alice");
        String toString = dto.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("content=Hello"));
        assertTrue(toString.contains("sender=Alice"));
    }

    @Test
    void testCanEqual() {
        MessageDTO dto = new MessageDTO();
        assertTrue(dto.canEqual(new MessageDTO()));
        assertFalse(dto.canEqual(new Object()));
    }
}