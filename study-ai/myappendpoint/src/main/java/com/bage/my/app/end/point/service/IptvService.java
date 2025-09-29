package com.bage.my.app.end.point.service;

import com.bage.my.app.end.point.entity.IptvChannel;
import java.util.List;
import com.bage.my.app.end.point.model.response.CategoryChannelsResponse;
import org.springframework.data.domain.Page;import org.springframework.data.domain.Pageable;
import java.util.Map;

public interface IptvService {
    List<IptvChannel> getAllChannels();
    CategoryChannelsResponse getChannels(List<String> tags);
    Page<IptvChannel> getChannelsByTagWithPagination(List<String> tags, Pageable pageable);
    Map<String, List<IptvChannel>> getChannelsByGroup(); // 原有方法
    Map<String, List<IptvChannel>> getChannelsByGroup(String language); // 新增方法 - 支持按语言过滤
    void loadIptvData();
    
    // 新增方法：添加喜欢频道
    void addFavoriteChannel(Long userId, int channelId);
    
    // 新增方法：获取用户喜欢的频道
    List<IptvChannel> getFavoriteChannels(Long userId);
}