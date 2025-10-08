package com.bage.my.app.end.point.model.response;

import lombok.Data;

/**
 * 频道响应对象
 */
@Data
public class Channel {
    private Long id;
    private String name;
    private String url;
    private String group;
    private String category;
    private String language;
    private String country;
    private String logo;
    private String tags;
    private Boolean isLike; // 是否喜欢
    
    public Channel() {
    }
    
    public Channel(com.bage.my.app.end.point.entity.IptvChannel iptvChannel) {
        this.id = iptvChannel.getId();
        this.name = iptvChannel.getName();
        this.url = iptvChannel.getUrl();
        this.group = iptvChannel.getGroup();
        this.category = iptvChannel.getCategory();
        this.language = iptvChannel.getLanguage();
        this.country = iptvChannel.getCountry();
        this.logo = iptvChannel.getLogo();
        this.tags = iptvChannel.getTags();
        this.isLike = false; // 默认未喜欢
    }
}