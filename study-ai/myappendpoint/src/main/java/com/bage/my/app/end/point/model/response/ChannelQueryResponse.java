package com.bage.my.app.end.point.model.response;

import java.util.List;

import com.bage.my.app.end.point.entity.IptvChannel;

import lombok.Data;

/**
 * 频道查询响应
 */
@Data
public class ChannelQueryResponse  extends PageResponse{
    private List<IptvChannel> channels;
    
    public ChannelQueryResponse() {
    }
    
    public ChannelQueryResponse(List<IptvChannel> channels) {
        this.channels = channels;
        if (channels != null) {
            // 计算统计信息
            this.setTotalElements(channels.size());
            this.setTotalChannels(channels.size());
            // 计算分类数量
            long categoryCount = channels.stream()
                .map(IptvChannel::getCategory)
                .filter(category -> category != null && !category.isEmpty())
                .distinct()
                .count();
            this.setTotalCategories((int) categoryCount);
        }
    }
    
    // 设置总频道数（兼容性方法）
    public void setTotalChannels(int totalChannels) {
        this.setTotalElements(totalChannels);
    }
    
    // 获取总频道数（兼容性方法）
    public int getTotalChannels() {
        return (int) this.getTotalElements();
    }
    
    // 设置总分类数
    public void setTotalCategories(int totalCategories) {
        this.setTotalPages(totalCategories);
    }
    
    // 获取总分类数
    public int getTotalCategories() {
        return this.getTotalPages();
    }
}