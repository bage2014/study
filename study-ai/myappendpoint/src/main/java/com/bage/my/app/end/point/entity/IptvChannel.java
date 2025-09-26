package com.bage.my.app.end.point.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class IptvChannel {
    private int id; // 新增ID字段
    private String name;
    private String url;
    private String group;
    private String category;
    private String language;
    private String country;
    private String logo;
    private List<String> tags = new ArrayList<>(); // 新增标签列表

    // 添加标签方法
    public void addTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    // 删除标签方法
    public void removeTag(String tag) {
        tags.remove(tag);
    }
}