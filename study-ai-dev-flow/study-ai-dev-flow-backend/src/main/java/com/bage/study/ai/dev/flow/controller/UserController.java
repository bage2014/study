package com.bage.study.ai.dev.flow.controller;

import com.bage.study.ai.dev.flow.dto.request.UserCreateRequest;
import com.bage.study.ai.dev.flow.dto.request.UserUpdateRequest;
import com.bage.study.ai.dev.flow.dto.response.UserResponse;
import com.bage.study.ai.dev.flow.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        log.debug("查询所有用户");
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        log.debug("查询用户, id: {}", id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        log.info("创建用户, username: {}", request.getUsername());
        UserResponse response = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody UserUpdateRequest request) {
        log.info("更新用户, id: {}", id);
        return ResponseEntity.ok(userService.update(id, request));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("删除用户, id: {}", id);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
