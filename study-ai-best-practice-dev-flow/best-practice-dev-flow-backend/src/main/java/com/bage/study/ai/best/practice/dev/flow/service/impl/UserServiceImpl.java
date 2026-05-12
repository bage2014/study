package com.bage.study.ai.best.practice.dev.flow.service.impl;

import com.bage.study.ai.best.practice.dev.flow.dto.request.LoginRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.request.RegisterRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.request.UserProfileRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.response.LoginResponse;
import com.bage.study.ai.best.practice.dev.flow.dto.response.UserDTO;
import com.bage.study.ai.best.practice.dev.flow.entity.User;
import com.bage.study.ai.best.practice.dev.flow.exception.AuthenticationException;
import com.bage.study.ai.best.practice.dev.flow.exception.BusinessException;
import com.bage.study.ai.best.practice.dev.flow.exception.ResourceNotFoundException;
import com.bage.study.ai.best.practice.dev.flow.repository.UserRepository;
import com.bage.study.ai.best.practice.dev.flow.service.UserService;
import com.bage.study.ai.best.practice.dev.flow.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("用户登录: username={}", request.getUsername());
        long startTime = System.currentTimeMillis();

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("用户名或密码错误"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthenticationException("用户名或密码错误");
        }

        String token = jwtTokenProvider.createToken(user.getId(), user.getUsername());
        long duration = System.currentTimeMillis() - startTime;
        log.info("用户登录成功: userId={}, duration={}ms", user.getId(), duration);

        return new LoginResponse(token, convertToDTO(user));
    }

    @Override
    public UserDTO register(RegisterRequest request) {
        log.info("用户注册: username={}, email={}", request.getUsername(), request.getEmail());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(400, "用户名已存在");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(400, "邮箱已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        log.info("用户注册成功: userId={}", savedUser.getId());

        return convertToDTO(savedUser);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
        return convertToDTO(user);
    }

    @Override
    public UserDTO updateProfile(Long userId, UserProfileRequest request) {
        log.info("更新用户资料: userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        User updatedUser = userRepository.save(user);
        log.info("用户资料更新成功: userId={}", updatedUser.getId());

        return convertToDTO(updatedUser);
    }

    @Override
    public User getCurrentUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setAvatar(user.getAvatar());
        dto.setPhone(user.getPhone());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}