package com.bage.my.app.end.point.model.response;

import com.bage.my.app.end.point.entity.IptvChannel;
import lombok.Data;
import java.util.List;

@Data
public class SearchChannelResponse {
    private List<IptvChannel> channels;
    private int totalCount;
    private String searchKeyword;

    public SearchChannelResponse(List<IptvChannel> channels, int totalCount, String searchKeyword) {
        this.channels = channels;
        this.totalCount = totalCount;
        this.searchKeyword = searchKeyword;
    }
}