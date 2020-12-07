package com.bage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class M3uItem {
    private String title;
    private String logo;
    private List<ChannelUrl> channelUrls;


}
