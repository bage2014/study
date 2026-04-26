package com.familytree.service;

import com.familytree.dto.LoginRequest;
import com.familytree.dto.RegisterRequest;
import com.familytree.exception.BusinessException;
import com.familytree.exception.ErrorCode;
import com.familytree.model.User;
import com.familytree.repository.UserRepository;
import com.familytree.utils.JwtUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public String login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            log.info("User found: {}", userOptional.get());
            User user = userOptional.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                log.info("Password matches");
                return jwtUtils.generateToken(user.getId());
            }
        }
        throw new BusinessException(ErrorCode.INVALID_CREDENTIALS, "Invalid email or password");
    }

    public User register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, "Email already exists");
        }

        if (request.getPhone() != null && userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new BusinessException(ErrorCode.PHONE_ALREADY_EXISTS, "Phone already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername() != null ? request.getUsername() : request.getEmail());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setCreatedAt(new Date());

        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "User not found"));
    }
}