package com.bage.my.app.end.point.repository;

import com.bage.my.app.end.point.entity.AppLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppLikeRepository extends JpaRepository<AppLike, Long> {
    // 根据用户和引用ID查找喜欢关系
    Optional<AppLike> findByUserIdAndRefId(Long userId, Long refId);

    // 删除用户对某个引用ID的喜欢
    void deleteByUserIdAndRefId(Long userId, Long refId);

    // 检查用户是否已喜欢某个引用ID
    boolean existsByUserIdAndRefId(Long userId, Long refId);
    
    // 根据用户ID查找所有喜欢的记录
    List<AppLike> findAllByUserId(Long userId);
}