package com.bage.my.app.end.point.model.response;

import com.bage.my.app.end.point.entity.IptvChannel;
import lombok.Data;
import java.util.List;

@Data
public class CategoryChannelsResponse {
    private List<IptvChannel> channels;
    private int totalCategories;
    private int totalChannels;

    public CategoryChannelsResponse() {}

    public CategoryChannelsResponse(List<IptvChannel> channels) {
        this.channels = channels;
        this.totalCategories = channels != null ? channels.size() : 0;
        this.totalChannels = channels != null ? 
            channels.stream().mapToInt(channel -> channel.getTags() != null ? channel.getTags().size() : 0).sum() : 0;
    }
}