package com.bage.my.app.end.point.model.response;

import com.bage.my.app.end.point.entity.IptvChannel;
import lombok.Data;
import java.util.List;

@Data
public class IptvChannelType {
    private String type;
    private String desc;
    private List<IptvChannel> channels;

    public IptvChannelType() {}

    public IptvChannelType(String type, String desc, List<IptvChannel> channels) {
        this.type = type;
        this.desc = desc;
        this.channels = channels;
    }
}