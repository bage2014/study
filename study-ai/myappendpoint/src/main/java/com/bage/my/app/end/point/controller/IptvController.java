package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.ApiResponse;
import com.bage.my.app.end.point.entity.IptvChannel;
import com.bage.my.app.end.point.model.response.CategoryChannelsResponse;
import com.bage.my.app.end.point.service.IptvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/categories")
    public ApiResponse<CategoryChannelsResponse> getChannelsByCategory() {
        try {
            CategoryChannelsResponse response = iptvService.getChannelsByCategory();
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("按分类获取频道失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "按分类获取频道失败");
        }
    }

    @GetMapping("/language/{language}")
    public ApiResponse<List<IptvChannel>> getChannelsByLanguage(@PathVariable String language) {
        try {
            List<IptvChannel> channels = iptvService.getChannelsByLanguage(language);
            return ApiResponse.success(channels);
        } catch (Exception e) {
            log.error("按语言获取频道失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "按语言获取频道失败");
        }
    }

    @GetMapping("/search")
    public ApiResponse<List<IptvChannel>> searchChannels(@RequestParam String keyword) {
        try {
            List<IptvChannel> channels = iptvService.searchChannels(keyword);
            return ApiResponse.success(channels);
        } catch (Exception e) {
            log.error("搜索频道失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "搜索频道失败");
        }
    }

    @GetMapping("/chinese")
    public ApiResponse<List<IptvChannel>> getChineseChannels() {
        try {
            List<IptvChannel> chineseChannels = iptvService.getChannelsByLanguage("chinese")
                    .stream()
                    .filter(channel -> channel.getCategory() != null && 
                            (channel.getCategory().equals("中文频道") || 
                             channel.getCategory().equals("电影") ||
                             channel.getCategory().equals("音乐") ||
                             channel.getCategory().equals("新闻") ||
                             channel.getCategory().equals("娱乐")))
                    .collect(java.util.stream.Collectors.toList());
            return ApiResponse.success(chineseChannels);
        } catch (Exception e) {
            log.error("获取中文频道失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "获取中文频道失败");
        }
    }

    @GetMapping("/english")
    public ApiResponse<List<IptvChannel>> getEnglishChannels() {
        try {
            List<IptvChannel> englishChannels = iptvService.getChannelsByLanguage("english")
                    .stream()
                    .filter(channel -> channel.getCategory() != null && 
                            (channel.getCategory().equals("英文频道") || 
                             channel.getCategory().equals("Movie") ||
                             channel.getCategory().equals("Music") ||
                             channel.getCategory().equals("News") ||
                             channel.getCategory().equals("Entertainment")))
                    .collect(java.util.stream.Collectors.toList());
            return ApiResponse.success(englishChannels);
        } catch (Exception e) {
            log.error("获取英文频道失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "获取英文频道失败");
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
}