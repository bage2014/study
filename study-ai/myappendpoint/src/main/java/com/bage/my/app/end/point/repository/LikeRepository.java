package com.bage.my.app.end.point.repository;

import com.bage.my.app.end.point.entity.Like;
import com.bage.my.app.end.point.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    // 根据用户和M3uEntry ID查找喜欢关系
    Optional<Like> findByUserAndM3uEntryId(User user, int m3uEntryId);

    // 删除用户对某个M3uEntry的喜欢
    void deleteByUserAndM3uEntryId(User user, int m3uEntryId);

    // 检查用户是否已喜欢某个M3uEntry
    boolean existsByUserAndM3uEntryId(User user, int m3uEntryId);
    
    // 根据用户ID查找所有喜欢的记录
    List<Like> findAllByUserId(Long userId);
}