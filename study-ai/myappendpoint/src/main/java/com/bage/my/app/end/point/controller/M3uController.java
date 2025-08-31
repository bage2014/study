package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.ApiResponse;
import com.bage.my.app.end.point.entity.M3uEntry;
import com.bage.my.app.end.point.service.LikeService;
import com.bage.my.app.end.point.util.AuthUtil;
import com.bage.my.app.end.point.util.M3uParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class M3uController {
    private List<M3uEntry> m3uEntries = new ArrayList<>();

    @Autowired
    private LikeService likeService;

    @PostConstruct
    public void init() throws IOException {
        m3uEntries = M3uParser.parse("m3u/index.m3u");
        m3uEntries.get(0).setUrl("https://stream-akamai.castr.com/5b9352dbda7b8c769937e459/live_2361c920455111ea85db6911fe397b9e/index.fmp4.m3u8");
    }

    @GetMapping("/m3u/query")
    public Page<M3uEntry> parseM3u(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword
    ) {
        List<M3uEntry> filteredEntries = m3uEntries;
        if (!keyword.isEmpty()) {
            filteredEntries = m3uEntries.stream()
                   .filter(entry -> entry.getTitle().contains(keyword))
                   .collect(Collectors.toList());
        }
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredEntries.size());
        if (start > end) {
            start = 0;
            end = 0;
        }
        List<M3uEntry> pageContent = filteredEntries.subList(start, end);
        return new PageImpl<>(pageContent, pageable, filteredEntries.size());
    }

    /**
     * 添加喜欢
     */
    @PostMapping("/m3u/{id}/like")
    public ApiResponse<String> addLike(@PathVariable int id) {
        try {
            Long userId = AuthUtil.getCurrentUserId();
            if (userId == null) {
                return new ApiResponse<>(401, "未登录", null);
            }

            likeService.addLike(userId, id);
            return new ApiResponse<>(200, "添加喜欢成功", null);
        } catch (RuntimeException e) {
            return new ApiResponse<>(400, e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(500, "服务器错误", null);
        }
    }

    /**
     * 移除喜欢
     */
    @DeleteMapping("/m3u/{id}/like")
    public ApiResponse<String> removeLike(@PathVariable int id) {
        try {
            Long userId = AuthUtil.getCurrentUserId();
            if (userId == null) {
                return new ApiResponse<>(401, "未登录", null);
            }

            likeService.removeLike(userId, id);
            return new ApiResponse<>(200, "移除喜欢成功", null);
        } catch (RuntimeException e) {
            return new ApiResponse<>(400, e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(500, "服务器错误", null);
        }
    }

    /**
     * 检查是否喜欢
     */
    @GetMapping("/m3u/{id}/isliked")
    public ApiResponse<Boolean> isLiked(@PathVariable int id) {
        try {
            Long userId = AuthUtil.getCurrentUserId();
            if (userId == null) {
                return new ApiResponse<>(401, "未登录", null);
            }

            boolean isLiked = likeService.isLiked(userId, id);
            return new ApiResponse<>(200, "查询成功", isLiked);
        } catch (RuntimeException e) {
            return new ApiResponse<>(400, e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(500, "服务器错误", null);
        }
    }
}