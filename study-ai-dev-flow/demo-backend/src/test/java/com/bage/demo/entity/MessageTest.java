package com.bage.demo.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void testNoArgsConstructor() {
        Message message = new Message();
        assertNull(message.getId());
        assertNull(message.getContent());
        assertNull(message.getSender());
    }

    @Test
    void testAllArgsConstructor() {
        Message message = new Message(1L, "Hello", "Alice");
        assertEquals(1L, message.getId());
        assertEquals("Hello", message.getContent());
        assertEquals("Alice", message.getSender());
    }

    @Test
    void testBuilder() {
        Message message = Message.builder()
                .id(2L)
                .content("World")
                .sender("Bob")
                .build();
        assertEquals(2L, message.getId());
        assertEquals("World", message.getContent());
        assertEquals("Bob", message.getSender());
    }

    @Test
    void testSettersAndGetters() {
        Message message = new Message();
        message.setId(3L);
        message.setContent("Test");
        message.setSender("Charlie");

        assertEquals(3L, message.getId());
        assertEquals("Test", message.getContent());
        assertEquals("Charlie", message.getSender());
    }

    @Test
    void testEqualsAndHashCode() {
        Message msg1 = new Message(1L, "Hello", "Alice");
        Message msg2 = new Message(1L, "Hello", "Alice");

        assertEquals(msg1, msg2);
        assertEquals(msg1.hashCode(), msg2.hashCode());
    }

    @Test
    void testNotEquals() {
        Message msg1 = new Message(1L, "Hello", "Alice");
        Message msg2 = new Message(2L, "World", "Bob");

        assertNotEquals(msg1, msg2);
    }

    @Test
    void testToString() {
        Message message = new Message(1L, "Hello", "Alice");
        String toString = message.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("content=Hello"));
        assertTrue(toString.contains("sender=Alice"));
    }

    @Test
    void testCanEqual() {
        Message message = new Message();
        assertTrue(message.canEqual(new Message()));
        assertFalse(message.canEqual(new Object()));
    }
}