package com.bage.my.app.end.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bage.my.app.end.point.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}