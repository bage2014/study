package com.bage.my.app.end.point.service;

import com.bage.my.app.end.point.entity.Like;
import com.bage.my.app.end.point.entity.User;
import com.bage.my.app.end.point.repository.LikeRepository;
import com.bage.my.app.end.point.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 添加喜欢
     */
    public void addLike(Long userId, int m3uEntryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 检查是否已经喜欢
        if (likeRepository.existsByUserAndM3uEntryId(user, m3uEntryId)) {
            throw new RuntimeException("已经喜欢该条目");
        }

        Like like = new Like();
        like.setUser(user);
        like.setM3uEntryId(m3uEntryId);
        likeRepository.save(like);
    }

    /**
     * 移除喜欢
     */
    public void removeLike(Long userId, int m3uEntryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 检查是否存在喜欢关系
        if (!likeRepository.existsByUserAndM3uEntryId(user, m3uEntryId)) {
            throw new RuntimeException("未找到喜欢关系");
        }

        likeRepository.deleteByUserAndM3uEntryId(user, m3uEntryId);
    }

    /**
     * 检查是否喜欢
     */
    public boolean isLiked(Long userId, int m3uEntryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        return likeRepository.existsByUserAndM3uEntryId(user, m3uEntryId);
    }

    /**
     * 获取用户喜欢的所有频道ID列表
     */
    public List<Like> findAllByUserId(Long userId) {
        // 查询用户的所有喜欢记录
        List<Like> likes = likeRepository.findAllByUserId(userId);
        
        // 提取m3uEntryId并返回
        return likes;
    }
}