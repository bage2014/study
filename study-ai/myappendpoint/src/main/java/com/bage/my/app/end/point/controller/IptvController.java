package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.ApiResponse;
import com.bage.my.app.end.point.entity.IptvChannel;
import com.bage.my.app.end.point.model.request.TagRequest;
import com.bage.my.app.end.point.model.response.CategoryChannelsResponse;
import com.bage.my.app.end.point.service.IptvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ApiResponse<CategoryChannelsResponse> getChannelsByTag(@RequestBody TagRequest request) {
        try {
            CategoryChannelsResponse response = iptvService.getChannels(request.getTags());
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("按标签获取频道失败: {}", e.getMessage(), e);
            return ApiResponse.fail(500, "按标签获取频道失败");
        }
    }
}