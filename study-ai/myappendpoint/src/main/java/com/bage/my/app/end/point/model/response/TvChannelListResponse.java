package com.bage.my.app.end.point.model.response;

import com.bage.my.app.end.point.entity.TvChannel;
import java.util.List;

public class TvChannelListResponse extends PageResponse {
    private List<TvChannel> channels;

    // 构造函数
    public TvChannelListResponse(List<TvChannel> channels, long totalElements, int totalPages, int currentPage, int pageSize) {
        super(totalElements, totalPages, currentPage, pageSize);
        this.channels = channels;
    }

    // Getters and Setters
    public List<TvChannel> getChannels() { return channels; }
    public void setChannels(List<TvChannel> channels) { this.channels = channels; }
}