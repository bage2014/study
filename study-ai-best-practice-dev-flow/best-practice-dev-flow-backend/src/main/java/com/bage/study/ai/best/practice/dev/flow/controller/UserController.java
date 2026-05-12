package com.bage.study.ai.best.practice.dev.flow.controller;

import com.bage.study.ai.best.practice.dev.flow.dto.LoginRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.LoginResponse;
import com.bage.study.ai.best.practice.dev.flow.dto.RegisterRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.UpdateUserRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.UserDTO;
import com.bage.study.ai.best.practice.dev.flow.service.UserService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final MeterRegistry meterRegistry;

    public UserController(UserService userService, MeterRegistry meterRegistry) {
        this.userService = userService;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping("/login")
    @Timed(value = "user.login", description = "用户登录操作耗时")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                                @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        String currentTraceId = initTraceId(traceId);
        long startTime = System.currentTimeMillis();
        log.info("用户登录请求: traceId={}, username={}", currentTraceId, loginRequest.getUsername());

        try {
            LoginResponse response = userService.login(loginRequest);
            long duration = System.currentTimeMillis() - startTime;
            log.info("用户登录成功: traceId={}, username={}, duration={}ms", 
                    currentTraceId, loginRequest.getUsername(), duration);
            meterRegistry.counter("user.login.success").increment();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("用户登录失败: traceId={}, username={}, duration={}ms, error={}", 
                    currentTraceId, loginRequest.getUsername(), duration, e.getMessage());
            meterRegistry.counter("user.login.failure").increment();
            throw e;
        } finally {
            MDC.remove("traceId");
        }
    }

    @PostMapping("/register")
    @Timed(value = "user.register", description = "用户注册操作耗时")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest registerRequest,
                                            @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        String currentTraceId = initTraceId(traceId);
        long startTime = System.currentTimeMillis();
        log.info("用户注册请求: traceId={}, username={}, email={}", 
                currentTraceId, registerRequest.getUsername(), registerRequest.getEmail());

        try {
            UserDTO createdUser = userService.register(registerRequest);
            long duration = System.currentTimeMillis() - startTime;
            log.info("用户注册成功: traceId={}, userId={}, username={}, duration={}ms", 
                    currentTraceId, createdUser.getId(), createdUser.getUsername(), duration);
            meterRegistry.counter("user.register.success").increment();
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("用户注册失败: traceId={}, username={}, duration={}ms, error={}", 
                    currentTraceId, registerRequest.getUsername(), duration, e.getMessage());
            meterRegistry.counter("user.register.failure").increment();
            throw e;
        } finally {
            MDC.remove("traceId");
        }
    }

    @GetMapping
    @Timed(value = "user.get.all", description = "获取所有用户列表耗时")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        String currentTraceId = initTraceId(traceId);
        long startTime = System.currentTimeMillis();
        log.info("获取用户列表请求: traceId={}", currentTraceId);

        try {
            List<UserDTO> users = userService.getAllUsers();
            long duration = System.currentTimeMillis() - startTime;
            log.info("获取用户列表成功: traceId={}, count={}, duration={}ms", 
                    currentTraceId, users.size(), duration);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("获取用户列表失败: traceId={}, duration={}ms, error={}", 
                    currentTraceId, duration, e.getMessage());
            throw e;
        } finally {
            MDC.remove("traceId");
        }
    }

    @GetMapping("/{id}")
    @Timed(value = "user.get.by.id", description = "根据ID获取用户耗时")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id,
                                               @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        String currentTraceId = initTraceId(traceId);
        long startTime = System.currentTimeMillis();
        log.info("获取用户信息请求: traceId={}, userId={}", currentTraceId, id);

        try {
            UserDTO user = userService.getUserById(id);
            long duration = System.currentTimeMillis() - startTime;
            log.info("获取用户信息成功: traceId={}, userId={}, username={}, duration={}ms", 
                    currentTraceId, user.getId(), user.getUsername(), duration);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("获取用户信息失败: traceId={}, userId={}, duration={}ms, error={}", 
                    currentTraceId, id, duration, e.getMessage());
            throw e;
        } finally {
            MDC.remove("traceId");
        }
    }

    @PutMapping("/{id}")
    @Timed(value = "user.update", description = "更新用户信息耗时")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, 
                                               @Valid @RequestBody UpdateUserRequest updateUserRequest,
                                               @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        String currentTraceId = initTraceId(traceId);
        long startTime = System.currentTimeMillis();
        log.info("更新用户信息请求: traceId={}, userId={}", currentTraceId, id);

        try {
            UserDTO updatedUser = userService.updateUser(id, updateUserRequest);
            long duration = System.currentTimeMillis() - startTime;
            log.info("更新用户信息成功: traceId={}, userId={}, username={}, duration={}ms", 
                    currentTraceId, updatedUser.getId(), updatedUser.getUsername(), duration);
            meterRegistry.counter("user.update.success").increment();
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("更新用户信息失败: traceId={}, userId={}, duration={}ms, error={}", 
                    currentTraceId, id, duration, e.getMessage());
            meterRegistry.counter("user.update.failure").increment();
            throw e;
        } finally {
            MDC.remove("traceId");
        }
    }

    @DeleteMapping("/{id}")
    @Timed(value = "user.delete", description = "删除用户耗时")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id,
                                           @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        String currentTraceId = initTraceId(traceId);
        long startTime = System.currentTimeMillis();
        log.info("删除用户请求: traceId={}, userId={}", currentTraceId, id);

        try {
            userService.deleteUser(id);
            long duration = System.currentTimeMillis() - startTime;
            log.info("删除用户成功: traceId={}, userId={}, duration={}ms", 
                    currentTraceId, id, duration);
            meterRegistry.counter("user.delete.success").increment();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("删除用户失败: traceId={}, userId={}, duration={}ms, error={}", 
                    currentTraceId, id, duration, e.getMessage());
            meterRegistry.counter("user.delete.failure").increment();
            throw e;
        } finally {
            MDC.remove("traceId");
        }
    }

    private String initTraceId(String traceId) {
        String currentTraceId = traceId != null ? traceId : UUID.randomUUID().toString();
        MDC.put("traceId", currentTraceId);
        return currentTraceId;
    }
}