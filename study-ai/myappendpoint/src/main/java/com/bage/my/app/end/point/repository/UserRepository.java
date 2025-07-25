package com.bage.my.app.end.point.repository;

import com.bage.my.app.end.point.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByToken(String token);
    User findByEmail(String email);
}