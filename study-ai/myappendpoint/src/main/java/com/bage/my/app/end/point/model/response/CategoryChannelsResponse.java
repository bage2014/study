package com.bage.my.app.end.point.model.response;

import lombok.Data;
import java.util.List;

@Data
public class CategoryChannelsResponse {
    private List<Channel> channels;
    private int totalCategories;
    private int totalChannels;

    public CategoryChannelsResponse() {}

    public CategoryChannelsResponse(List<Channel> channels) {
        this.channels = channels;
        this.totalCategories = channels != null ? channels.size() : 0;
        this.totalChannels = channels != null ? 
            channels.stream().mapToInt(channel -> channel.getTags() != null ? 
                channel.getTags().split(",").length : 0).sum() : 0;
    }
}