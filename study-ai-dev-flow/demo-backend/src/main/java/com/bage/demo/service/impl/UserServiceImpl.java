package com.bage.demo.service.impl;

import com.bage.demo.entity.User;
import com.bage.demo.repository.UserRepository;
import com.bage.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,50}$");

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setPassword(user.getPassword());
        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            log.warn("用户标识符为空");
            throw new IllegalArgumentException("用户标识符不能为空");
        }
        if (!USER_ID_PATTERN.matcher(userId).matches()) {
            log.warn("用户标识符格式无效: {}", userId);
            throw new IllegalArgumentException("用户标识符格式无效，必须为3-50位的字母、数字或下划线");
        }
        log.debug("用户标识符验证通过: {}", userId);
    }

    @Override
    public boolean userExists(String userId) {
        validateUserId(userId);
        return true;
    }
}