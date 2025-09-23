package com.bage.my.app.end.point.service;

import com.bage.my.app.end.point.entity.IptvChannel;
import java.util.List;
import java.util.Map;

public interface IptvService {
    List<IptvChannel> getAllChannels();
    Map<String, List<IptvChannel>> getChannelsByCategory();
    List<IptvChannel> getChannelsByLanguage(String language);
    List<IptvChannel> searchChannels(String keyword);
    void loadIptvData();
}