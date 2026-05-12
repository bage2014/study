package com.bage.study.ai.best.practice.dev.flow.service.impl;

import com.bage.study.ai.best.practice.dev.flow.dto.LoginRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.LoginResponse;
import com.bage.study.ai.best.practice.dev.flow.dto.RegisterRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.UpdateUserRequest;
import com.bage.study.ai.best.practice.dev.flow.dto.UserDTO;
import com.bage.study.ai.best.practice.dev.flow.entity.User;
import com.bage.study.ai.best.practice.dev.flow.exception.UserNotFoundException;
import com.bage.study.ai.best.practice.dev.flow.exception.UsernameExistsException;
import com.bage.study.ai.best.practice.dev.flow.exception.EmailExistsException;
import com.bage.study.ai.best.practice.dev.flow.exception.AuthenticationException;
import com.bage.study.ai.best.practice.dev.flow.repository.UserRepository;
import com.bage.study.ai.best.practice.dev.flow.service.UserService;
import com.bage.study.ai.best.practice.dev.flow.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        log.debug("登录验证: username={}", loginRequest.getUsername());
        
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new AuthenticationException("用户名或密码错误"));
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("登录失败: 密码不匹配, username={}", loginRequest.getUsername());
            throw new AuthenticationException("用户名或密码错误");
        }
        
        log.info("登录成功: userId={}, username={}", user.getId(), user.getUsername());
        return convertToLoginResponse(user);
    }

    @Override
    public UserDTO register(RegisterRequest registerRequest) {
        log.debug("注册验证: username={}, email={}", registerRequest.getUsername(), registerRequest.getEmail());
        
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            log.warn("注册失败: 用户名已存在, username={}", registerRequest.getUsername());
            throw new UsernameExistsException("用户名已存在");
        }
        
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.warn("注册失败: 邮箱已存在, email={}", registerRequest.getEmail());
            throw new EmailExistsException("邮箱已存在");
        }
        
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        
        User savedUser = userRepository.save(user);
        log.info("注册成功: userId={}, username={}", savedUser.getId(), savedUser.getUsername());
        
        return convertToDTO(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        log.debug("获取所有用户列表");
        List<User> users = userRepository.findAll();
        log.debug("获取用户列表完成: count={}", users.size());
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        log.debug("获取用户信息: userId={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("用户不存在: " + id));
        log.debug("获取用户信息成功: userId={}, username={}", user.getId(), user.getUsername());
        return convertToDTO(user);
    }

    @Override
    public UserDTO updateUser(Long id, UpdateUserRequest updateUserRequest) {
        log.debug("更新用户信息: userId={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("用户不存在: " + id));
        
        if (updateUserRequest.getUsername() != null && !updateUserRequest.getUsername().isEmpty()) {
            if (!user.getUsername().equals(updateUserRequest.getUsername()) && 
                userRepository.existsByUsername(updateUserRequest.getUsername())) {
                log.warn("更新失败: 用户名已存在, username={}", updateUserRequest.getUsername());
                throw new UsernameExistsException("用户名已存在");
            }
            user.setUsername(updateUserRequest.getUsername());
        }
        
        if (updateUserRequest.getEmail() != null && !updateUserRequest.getEmail().isEmpty()) {
            if (!user.getEmail().equals(updateUserRequest.getEmail()) && 
                userRepository.existsByEmail(updateUserRequest.getEmail())) {
                log.warn("更新失败: 邮箱已存在, email={}", updateUserRequest.getEmail());
                throw new EmailExistsException("邮箱已存在");
            }
            user.setEmail(updateUserRequest.getEmail());
        }
        
        if (updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }
        
        User updatedUser = userRepository.save(user);
        log.info("更新用户信息成功: userId={}, username={}", updatedUser.getId(), updatedUser.getUsername());
        
        return convertToDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.debug("删除用户: userId={}", id);
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("用户不存在: " + id);
        }
        userRepository.deleteById(id);
        log.info("删除用户成功: userId={}", id);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    private LoginResponse convertToLoginResponse(User user) {
        String token = jwtUtil.generateToken(user.getUsername(), user.getId());
        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                token
        );
    }
}