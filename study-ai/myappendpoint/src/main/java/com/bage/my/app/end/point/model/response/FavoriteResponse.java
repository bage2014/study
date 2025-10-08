package com.bage.my.app.end.point.model.response;

import lombok.Data;
import java.util.List;

@Data
public class FavoriteResponse {
    private List<Channel> channels;
    private int totalChannels;
    private int currentPage;
    private int pageSize;
    private int totalPages;

    public FavoriteResponse() {}

    public FavoriteResponse(List<Channel> channels, int currentPage, int pageSize, int totalChannels) {
        this.channels = channels;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalChannels = totalChannels;
        this.totalPages = (int) Math.ceil((double) totalChannels / pageSize);
    }
}