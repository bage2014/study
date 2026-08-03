package com.bage.demo.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    void noArgsConstructor_shouldCreateEmptyDTO() {
        UserDTO dto = new UserDTO();

        assertNotNull(dto);
        assertNull(dto.getName());
        assertNull(dto.getEmail());
    }

    @Test
    void setters_shouldUpdateFields() {
        UserDTO dto = new UserDTO();
        dto.setName("Alice");
        dto.setEmail("alice@example.com");

        assertEquals("Alice", dto.getName());
        assertEquals("alice@example.com", dto.getEmail());
    }

    @Test
    void equals_shouldCompareFields() {
        UserDTO dto1 = new UserDTO();
        dto1.setName("Alice");
        dto1.setEmail("alice@example.com");

        UserDTO dto2 = new UserDTO();
        dto2.setName("Alice");
        dto2.setEmail("alice@example.com");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_shouldContainFields() {
        UserDTO dto = new UserDTO();
        dto.setName("Alice");
        dto.setEmail("alice@example.com");

        String str = dto.toString();

        assertNotNull(str);
        assertTrue(str.contains("Alice"));
        assertTrue(str.contains("alice@example.com"));
    }
}