package com.bage.my.app.end.point.model.response;

import com.bage.my.app.end.point.entity.IptvChannel;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class GroupedChannelsResponse {
    private Map<String, List<IptvChannel>> channelsByGroup; // 分组名称 -> 频道列表
    private int totalGroups; // 分组总数
    private int totalChannels; // 频道总数
    
    public GroupedChannelsResponse(Map<String, List<IptvChannel>> channelsByGroup) {
        this.channelsByGroup = channelsByGroup;
        this.totalGroups = channelsByGroup.size();
        this.totalChannels = channelsByGroup.values().stream()
            .mapToInt(List::size)
            .sum();
    }
}