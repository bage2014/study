package com.bage.demo.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void builder_shouldCreateUser() {
        User user = User.builder()
                .id(1L)
                .name("Alice")
                .email("alice@example.com")
                .build();

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("Alice", user.getName());
        assertEquals("alice@example.com", user.getEmail());
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyUser() {
        User user = new User();

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
    }

    @Test
    void allArgsConstructor_shouldCreateUser() {
        User user = new User(1L, "Bob", "bob@example.com");

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("Bob", user.getName());
        assertEquals("bob@example.com", user.getEmail());
    }

    @Test
    void setters_shouldUpdateFields() {
        User user = new User();
        user.setId(2L);
        user.setName("Charlie");
        user.setEmail("charlie@example.com");

        assertEquals(2L, user.getId());
        assertEquals("Charlie", user.getName());
        assertEquals("charlie@example.com", user.getEmail());
    }

    @Test
    void equals_shouldCompareByIdAndFields() {
        User user1 = User.builder().id(1L).name("Alice").email("alice@example.com").build();
        User user2 = User.builder().id(1L).name("Alice").email("alice@example.com").build();

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void toString_shouldContainFields() {
        User user = User.builder().id(1L).name("Alice").email("alice@example.com").build();

        String str = user.toString();

        assertNotNull(str);
        assertTrue(str.contains("Alice"));
        assertTrue(str.contains("alice@example.com"));
    }
}