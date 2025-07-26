package com.bage.my.app.end.point.repository;

import com.bage.my.app.end.point.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    UserToken findByToken(String token);
    UserToken findByUserId(Long userId);
    void deleteByUserId(Long userId);
}