package com.bage.my.app.end.point.model.response;

import com.bage.my.app.end.point.entity.IptvChannel;
import lombok.Data;
import java.util.List;

@Data
public class CategoryChannelsResponse {
    private List<IptvChannelType> categories;
    private int totalCategories;
    private int totalChannels;

    public CategoryChannelsResponse() {}

    public CategoryChannelsResponse(List<IptvChannelType> categories) {
        this.categories = categories;
        this.totalCategories = categories != null ? categories.size() : 0;
        this.totalChannels = categories != null ? 
            categories.stream().mapToInt(type -> type.getChannels() != null ? type.getChannels().size() : 0).sum() : 0;
    }
}