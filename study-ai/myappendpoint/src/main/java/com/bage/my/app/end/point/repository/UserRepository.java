package com.bage.my.app.end.point.repository;

import com.bage.my.app.end.point.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);

    // 新增：根据关键词分页查询用户
    @Query("SELECT u FROM User u WHERE " +
           "(:keyword IS NULL OR " +
           "u.username LIKE %:keyword% OR " +
           "u.email LIKE %:keyword%)")
    Page<User> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}