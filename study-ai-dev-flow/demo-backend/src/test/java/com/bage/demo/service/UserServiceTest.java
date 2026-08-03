package com.bage.demo.service;

import com.bage.demo.dto.UserDTO;
import com.bage.demo.entity.User;
import com.bage.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        List<User> users = Arrays.asList(
                User.builder().id(1L).name("Alice").email("alice@example.com").build(),
                User.builder().id(2L).name("Bob").email("bob@example.com").build()
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
    }

    @Test
    void getUserById_whenExists_shouldReturnUser() {
        User user = User.builder().id(1L).name("Alice").email("alice@example.com").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Alice", result.getName());
    }

    @Test
    void getUserById_whenNotExists_shouldReturnNull() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        User result = userService.getUserById(99L);

        assertNull(result);
    }

    @Test
    void createUser_shouldSaveAndReturnUser() {
        UserDTO dto = new UserDTO();
        dto.setName("New User");
        dto.setEmail("new@example.com");

        User saved = User.builder().id(3L).name("New User").email("new@example.com").build();
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("New User", result.getName());
        assertEquals("new@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_whenExists_shouldUpdateAndReturnUser() {
        User existing = User.builder().id(1L).name("Old").email("old@example.com").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));

        UserDTO dto = new UserDTO();
        dto.setName("Updated");
        dto.setEmail("updated@example.com");

        User updated = User.builder().id(1L).name("Updated").email("updated@example.com").build();
        when(userRepository.save(any(User.class))).thenReturn(updated);

        User result = userService.updateUser(1L, dto);

        assertNotNull(result);
        assertEquals("Updated", result.getName());
        assertEquals("updated@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_whenNotExists_shouldReturnNull() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        UserDTO dto = new UserDTO();
        dto.setName("Updated");
        dto.setEmail("updated@example.com");

        User result = userService.updateUser(99L, dto);

        assertNull(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_whenExists_shouldDelete() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_whenNotExists_shouldDoNothing() {
        when(userRepository.existsById(99L)).thenReturn(false);

        userService.deleteUser(99L);

        verify(userRepository, never()).deleteById(anyLong());
    }
}