package com.bage.my.app.end.point.service;

import com.bage.my.app.end.point.entity.Like;
import com.bage.my.app.end.point.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    /**
     * 添加喜欢
     */
    public void addLike(Long userId, Long m3uEntryId) {
        // 检查是否已经喜欢
        if (likeRepository.existsByUserIdAndM3uEntryId(userId, m3uEntryId)) {
            throw new RuntimeException("已经喜欢该条目");
        }

        Like like = new Like();
        like.setUserId(userId);
        like.setM3uEntryId(m3uEntryId);
        likeRepository.save(like);
    }

    /**
     * 移除喜欢
     */
    public void removeLike(Long userId, Long m3uEntryId) {
        // 先查询喜欢关系
        Optional<Like> likeOptional = likeRepository.findByUserIdAndM3uEntryId(userId, m3uEntryId);
        
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
        return likeRepository.existsByUserIdAndM3uEntryId(userId, m3uEntryId);
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