package com.bage.my.app.end.point.service;

import com.bage.my.app.end.point.entity.IptvChannel;
import java.util.List;
import com.bage.my.app.end.point.model.response.CategoryChannelsResponse;
public interface IptvService {
    List<IptvChannel> getAllChannels();
    CategoryChannelsResponse getChannelsByCategory(List<String> tags);    
    void loadIptvData();
}