package com.bage.demo.controller;

import com.bage.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 验证用户标识符
     *
     * @param request 包含 userId 的请求体
     * @return 验证结果
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateUser(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        try {
            userService.validateUserId(userId);
            boolean exists = userService.userExists(userId);
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "exists", exists,
                    "userId", userId
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "valid", false,
                    "error", e.getMessage()
            ));
        }
    }

    /**
     * 检查用户是否存在
     *
     * @param userId 用户ID路径变量
     * @return 存在性检查结果
     */
    @GetMapping("/{userId}/exists")
    public ResponseEntity<Map<String, Object>> checkUserExists(@PathVariable String userId) {
        try {
            boolean exists = userService.userExists(userId);
            return ResponseEntity.ok(Map.of(
                    "exists", exists,
                    "userId", userId
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "exists", false,
                    "error", e.getMessage()
            ));
        }
    }
}