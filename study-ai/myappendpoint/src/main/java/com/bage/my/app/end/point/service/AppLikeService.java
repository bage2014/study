package com.bage.my.app.end.point.service;

import com.bage.my.app.end.point.entity.AppLike;
import com.bage.my.app.end.point.repository.AppLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppLikeService {
    @Autowired
    private AppLikeRepository likeRepository;

    /**
     * 添加喜欢
     */
    public void addLike(Long userId, Long m3uEntryId) {
        // 检查是否已经喜欢
        if (likeRepository.existsByUserIdAndRefId(userId, m3uEntryId)) {
            throw new RuntimeException("已经喜欢该条目");
        }

        AppLike like = new AppLike();
        like.setUserId(userId);
        like.setRefId(m3uEntryId);
        like.setLikeType("m3uEntry");
        likeRepository.save(like);
    }

    /**
     * 移除喜欢
     */
    public void removeLike(Long userId, Long m3uEntryId) {
        // 先查询喜欢关系
        Optional<AppLike> likeOptional = likeRepository.findByUserIdAndRefId(userId, m3uEntryId);
        
        // 检查是否存在喜欢关系
        if (!likeOptional.isPresent()) {
            throw new RuntimeException("未找到喜欢关系");
        }

        // 使用JpaRepository的delete方法删除已找到的实体
        likeRepository.delete(likeOptional.get());
    }

    /**
     * 检查是否喜欢
     */
    public boolean isLiked(Long userId, Long m3uEntryId) {
        return likeRepository.existsByUserIdAndRefId(userId, m3uEntryId);
    }

    /**
     * 获取用户喜欢的所有频道ID列表
     */
    public List<AppLike> findAllByUserId(Long userId) {
        // 查询用户的所有喜欢记录
        List<AppLike> likes = likeRepository.findAllByUserId(userId);
        
        // 提取m3uEntryId并返回
        return likes;
    }
}