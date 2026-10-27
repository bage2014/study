package com.bage.study.ai.dev.flow.service.impl;

import com.bage.study.ai.dev.flow.dto.request.UserCreateRequest;
import com.bage.study.ai.dev.flow.dto.request.UserUpdateRequest;
import com.bage.study.ai.dev.flow.dto.response.UserResponse;
import com.bage.study.ai.dev.flow.entity.UserEntity;
import com.bage.study.ai.dev.flow.exception.BusinessException;
import com.bage.study.ai.dev.flow.repository.UserRepository;
import com.bage.study.ai.dev.flow.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserResponse> findAll() {
        log.info("查询所有用户");
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    @Override
    public UserResponse findById(Long id) {
        log.info("根据ID查询用户, id: {}", id);
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("NOT_FOUND", "用户不存在, id: " + id));
        return UserResponse.fromEntity(entity);
    }

    @Override
    @Transactional
    public UserResponse create(UserCreateRequest request) {
        log.info("创建用户, username: {}, email: {}", request.getUsername(), request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("DUPLICATE_EMAIL", "邮箱已存在: " + request.getEmail());
        }

        UserEntity entity = new UserEntity();
        entity.setUsername(request.getUsername());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());

        UserEntity saved = userRepository.save(entity);
        log.debug("用户创建成功, id: {}", saved.getId());
        return UserResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        log.info("更新用户, id: {}", id);
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("NOT_FOUND", "用户不存在, id: " + id));

        if (request.getUsername() != null) {
            entity.setUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            if (userRepository.existsByEmail(request.getEmail()) && !request.getEmail().equals(entity.getEmail())) {
                throw new BusinessException("DUPLICATE_EMAIL", "邮箱已存在: " + request.getEmail());
            }
            entity.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            entity.setPhone(request.getPhone());
        }

        UserEntity updated = userRepository.save(entity);
        return UserResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("删除用户, id: {}", id);
        if (!userRepository.existsById(id)) {
            throw new BusinessException("NOT_FOUND", "用户不存在, id: " + id);
        }
        userRepository.deleteById(id);
    }
}
