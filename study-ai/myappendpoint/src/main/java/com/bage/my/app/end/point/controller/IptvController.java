package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.ApiResponse;
import com.bage.my.app.end.point.entity.IptvChannel;
import com.bage.my.app.end.point.model.request.TagRequest;
import com.bage.my.app.end.point.model.response.CategoryChannelsResponse;
import com.bage.my.app.end.point.model.response.GroupedChannelsResponse;
import com.bage.my.app.end.point.service.IptvService;
import com.bage.my.app.end.point.util.AuthUtil;
import com.bage.my.app.end.point.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/iptv")
@Slf4j
public class IptvController {

    private final IptvService iptvService;

    public IptvController(IptvService iptvService) {
        this.iptvService = iptvService;
    }

    @GetMapping("/channels")
    public ApiResponse<List<IptvChannel>> getAllChannels() {
        try {
            List<IptvChannel> channels = iptvService.getAllChannels();
            return ApiResponse.success(channels);
        } catch (Exception e) {
            log.error("获取所有频道失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "获取所有频道失败");
        }
    }

    @PostMapping("/reload")
    public ApiResponse<String> reloadData() {
        try {
            iptvService.loadIptvData();
            return ApiResponse.success("数据重新加载成功");
        } catch (Exception e) {
            log.error("重新加载数据失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "重新加载数据失败");
        }
    }

    @RequestMapping("/query/tags")
    public ApiResponse<CategoryChannelsResponse> getChannelsByTag(
            @RequestBody TagRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            log.info("按标签获取频道, 标签: {}, 页码: {}, 每页数量: {}", JsonUtil.toJson(request.getTags()), page, size);
            Pageable pageable = PageRequest.of(page, size);
            Page<IptvChannel> channelsPage = iptvService.getChannelsByTagWithPagination(request.getTags(), pageable);
            
            // 创建带有分页信息的响应
            CategoryChannelsResponse response = new CategoryChannelsResponse(channelsPage.getContent());
            response.setTotalCategories((int) channelsPage.getTotalElements());
            response.setTotalChannels((int) channelsPage.getTotalElements());
            
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("按标签获取频道失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "按标签获取频道失败");
        }
    }


    @RequestMapping("/query/group")
    public ApiResponse<GroupedChannelsResponse> getChannelsByGroup(
            @RequestParam(required = false, defaultValue = "") String keyword) {
        try {
            Map<String, List<IptvChannel>> channelsByGroup = iptvService.getChannelsByGroup(keyword);
            GroupedChannelsResponse response = new GroupedChannelsResponse(channelsByGroup);
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("获取分组频道失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "获取分组频道失败");
        }
    }
    
    /**
     * 添加喜欢的频道
     */
    @PostMapping("/favorite/add/{channelId}")
    public ApiResponse<String> addFavoriteChannel(@PathVariable int channelId) {
        try {
            Long userId = AuthUtil.getCurrentUserId();
            if (userId == null) {
                return ApiResponse.fail(401, "用户未登录");
            }
            
            iptvService.addFavoriteChannel(userId, channelId);
            return ApiResponse.success("添加喜欢频道成功");
        } catch (Exception e) {
            log.error("添加喜欢频道失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "添加喜欢频道失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取当前用户喜欢的所有频道
     */
    @GetMapping("/favorite/list")
    public ApiResponse<List<IptvChannel>> getFavoriteChannels() {
        try {
            Long userId = AuthUtil.getCurrentUserId();
            if (userId == null) {
                return ApiResponse.fail(401, "用户未登录");
            }
            
            List<IptvChannel> favoriteChannels = iptvService.getFavoriteChannels(userId);
            return ApiResponse.success(favoriteChannels);
        } catch (Exception e) {
            log.error("获取喜欢频道失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "获取喜欢频道失败: " + e.getMessage());
        }
    }
}