package com.bage.study.ai.best.practice.dev.flow.service.impl;

import com.bage.study.ai.best.practice.dev.flow.dto.LoginRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.LoginResponse;
import com.bage.study.ai.best.practice.dev.flow.dto.RegisterRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.UpdateUserRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.UserDTO;
import com.bage.study.ai.best.practice.dev.flow.entity.User;
import com.bage.study.ai.best.practice.dev.flow.repository.UserRepository;
import com.bage.study.ai.best.practice.dev.flow.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        
        return convertToLoginResponse(user);
    }

    @Override
    public UserDTO register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public UserDTO updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        if (updateUserRequest.getUsername() != null && !updateUserRequest.getUsername().isEmpty()) {
            if (!user.getUsername().equals(updateUserRequest.getUsername()) && 
                userRepository.existsByUsername(updateUserRequest.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
            user.setUsername(updateUserRequest.getUsername());
        }
        
        if (updateUserRequest.getEmail() != null && !updateUserRequest.getEmail().isEmpty()) {
            if (!user.getEmail().equals(updateUserRequest.getEmail()) && 
                userRepository.existsByEmail(updateUserRequest.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(updateUserRequest.getEmail());
        }
        
        if (updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }
        
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    private LoginResponse convertToLoginResponse(User user) {
        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}