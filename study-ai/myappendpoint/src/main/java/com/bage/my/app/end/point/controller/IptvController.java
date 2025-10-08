package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.ApiResponse;
import com.bage.my.app.end.point.entity.IptvChannel;
import com.bage.my.app.end.point.model.request.TagRequest;
import com.bage.my.app.end.point.model.response.CategoryChannelsResponse;
import com.bage.my.app.end.point.model.response.Channel;
import com.bage.my.app.end.point.model.response.ChannelQueryResponse;
import com.bage.my.app.end.point.model.response.GroupedChannelsResponse;
import com.bage.my.app.end.point.model.response.FavoriteResponse;
import com.bage.my.app.end.point.service.IptvService;
import com.bage.my.app.end.point.util.AuthUtil;
import com.bage.my.app.end.point.util.JsonUtil;
import com.bage.my.app.end.point.model.request.SearchRequest;

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

    @RequestMapping("/channels")
    public ApiResponse<ChannelQueryResponse> getAllChannels(@RequestBody SearchRequest request) {
        try {
            log.info("查询频道, 请求参数: {}", request);
            ChannelQueryResponse response = iptvService.getChannels(List.of(request.getKeyword()));
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("获取频道失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "获取频道失败");
        }
    }

    @RequestMapping("/query/tags")
    public ApiResponse<CategoryChannelsResponse> getChannelsByTag(
            @RequestBody TagRequest request) {
        try {
            log.info("按标签获取频道, 标签: {}, 页码: {}, 每页数量: {}", JsonUtil.toJson(request.getTags()), request.getPage(), request.getSize());
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
            Page<Channel> channelsPage = iptvService.getChannelsByTagWithPagination(request.getTags(), pageable);
            
            // 创建带有分页信息的响应
            CategoryChannelsResponse response = new CategoryChannelsResponse(channelsPage.getContent());
            response.setTotalCategories((int) channelsPage.getTotalElements());
            response.setTotalChannels((int) channelsPage.getTotalElements());
            log.info("按标签获取频道成功, 标签: {}, 页码: {}, 每页数量: {}, 总频道数: {}, 总分类数: {}", JsonUtil.toJson(request.getTags()), request.getPage(), request.getSize(), response.getTotalChannels(), response.getTotalCategories());
            log.info("按标签获取频道成功, 频道列表: {}", JsonUtil.toJson(response.getChannels()));
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("按标签获取频道失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "按标签获取频道失败");
        }
    }


    @RequestMapping("/query/group")
    public ApiResponse<GroupedChannelsResponse> getChannelsByGroup(
            @RequestBody TagRequest request) {
        try {  
            List<String> tags = request.getTags();
            if (tags == null || tags.isEmpty()) {
                tags = List.of("");
            }
            String tag = tags.get(0);
            tag = tag == null ? "" : tag.trim();
            Map<String, List<IptvChannel>> channelsByGroup = iptvService.getChannelsByGroup(tag);
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
    public ApiResponse<String> addFavoriteChannel(@PathVariable Long channelId) {
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
     * 删除喜欢的频道
     */
    @RequestMapping("/favorite/remove/{channelId}")
    public ApiResponse<String> removeFavoriteChannel(@PathVariable Long channelId) {
        try {
            Long userId = AuthUtil.getCurrentUserId();
            if (userId == null) {
                return ApiResponse.fail(401, "用户未登录");
            }
            
            iptvService.removeFavoriteChannel(userId, channelId);
            return ApiResponse.success("删除喜欢频道成功");
        } catch (Exception e) {
            log.error("删除喜欢频道失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "删除喜欢频道失败: " + e.getMessage());
        }
    }


    /**
     * 获取当前用户喜欢的所有频道(分页和过滤)
     */
    @RequestMapping("/favorite/list")
    public ApiResponse<FavoriteResponse> getFavoriteChannels(@RequestBody SearchRequest request) {
        try {
            Long userId = AuthUtil.getCurrentUserId();
            if (userId == null) {
                return ApiResponse.fail(401, "用户未登录");
            }
            
            log.info("获取用户喜欢的频道(分页): userId={}, 请求参数: {}", userId, JsonUtil.toJson(request));
            
            Page<Channel> favoriteChannelsPage = iptvService.getFavoriteChannelsWithPagination(userId, request);
            
            // 创建FavoriteResponse对象
            FavoriteResponse response = new FavoriteResponse(
                favoriteChannelsPage.getContent(),
                request.getPage(),
                request.getSize(),
                (int) favoriteChannelsPage.getTotalElements()
            );
            
            log.info("获取用户喜欢的频道(分页)成功: userId={}, 页码: {}, 每页数量: {}, 总数: {}", 
                    userId, request.getPage(), request.getSize(), favoriteChannelsPage.getTotalElements());
            
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("获取喜欢频道失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "获取喜欢频道失败: " + e.getMessage());
        }
    }
    
}