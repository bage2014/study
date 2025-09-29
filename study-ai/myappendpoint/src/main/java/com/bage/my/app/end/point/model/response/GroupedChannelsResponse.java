package com.bage.my.app.end.point.model.response;

import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bage.my.app.end.point.entity.IptvChannel;

@Data
public class GroupedChannelsResponse {
    private List<GroupedItem> groups;
    
    public GroupedChannelsResponse(Map<String, List<IptvChannel>> channelsByGroup) {
        // 将 Map 转换为 List<GroupedItem>
        this.groups = channelsByGroup.entrySet().stream()
                .map(entry -> new GroupedItem(entry.getKey(), entry.getValue().size()))
                .collect(Collectors.toList());
    }
    
    @Data
    public static class GroupedItem {
        private String groupName;
        private int totalChannels;
        
        public GroupedItem(String groupName, int totalChannels) {
            this.groupName = groupName;
            this.totalChannels = totalChannels;
        }
    }
}