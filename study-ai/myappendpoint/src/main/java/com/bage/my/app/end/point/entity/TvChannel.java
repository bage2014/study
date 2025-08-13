package com.bage.my.app.end.point.entity;

import lombok.Data;
import java.util.List;

@Data
public class TvChannel {
    private String title;
    private String logo;
    private List<ChannelUrl> channelUrls;

    @Data
    public static class ChannelUrl {
        private String title;
        private String url;
    }
}