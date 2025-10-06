package com.bage.my.app.end.point.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "iptv_channel")
public class IptvChannel {
    @Id
    private Long id;
    private String name;
    @Column(length = 1024)
    private String url;
    
    // 修改字段名以避免与SQL关键字冲突
    @Column(name = "channel_group")
    private String group;
    
    private String category;
    private String language;
    private String country;
    @Column(length = 1024)
    private String logo;
    
    private String tags; // 标签列表

    // 添加标签方法
    public void addTag(String tag) {
        if (tag == null || tag.isEmpty()) {
            return;
        }
        if (tags == null) {
            tags = "";
        }
        if (!tags.contains(tag)) {
            tags += tag + ",";
        }
    }

    // 删除标签方法
    public void removeTag(String tag) {
        if (tag == null || tag.isEmpty()) {
            return;
        }
        if (tags == null) {
            return;
        }
        if (tags.contains(tag + ",")) {
            tags = tags.replace(tag + ",", "");
        }
    }
}