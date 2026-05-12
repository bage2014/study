package com.bage.study.ai.best.practice.dev.flow.controller;

import com.bage.study.ai.best.practice.dev.flow.dto.LoginRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.LoginResponse;
import com.bage.study.ai.best.practice.dev.flow.dto.RegisterRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.UpdateUserRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.UserDTO;
import com.bage.study.ai.best.practice.dev.flow.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest registerRequest) {
        UserDTO createdUser = userService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}