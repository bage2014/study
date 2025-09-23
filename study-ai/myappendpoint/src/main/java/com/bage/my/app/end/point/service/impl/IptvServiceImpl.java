package com.bage.my.app.end.point.service.impl;

import com.bage.my.app.end.point.entity.IptvChannel;
import com.bage.my.app.end.point.service.IptvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IptvServiceImpl implements IptvService {

    private List<IptvChannel> allChannels = new ArrayList<>();
    private final String IPTV_URL = "https://iptv-org.github.io/iptv/index.m3u";

    @Override
    public List<IptvChannel> getAllChannels() {
        if (allChannels.isEmpty()) {
            loadIptvData();
        }
        return allChannels;
    }

    @Override
    public Map<String, List<IptvChannel>> getChannelsByCategory() {
        List<IptvChannel> channels = getAllChannels();
        return channels.stream()
                .filter(channel -> channel.getCategory() != null && !channel.getCategory().isEmpty())
                .collect(Collectors.groupingBy(IptvChannel::getCategory));
    }

    @Override
    public List<IptvChannel> getChannelsByLanguage(String language) {
        return getAllChannels().stream()
                .filter(channel -> channel.getLanguage() != null && 
                                 (channel.getLanguage().equalsIgnoreCase(language) ||
                                  channel.getLanguage().toLowerCase().contains(language.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public List<IptvChannel> searchChannels(String keyword) {
        return getAllChannels().stream()
                .filter(channel -> 
                    (channel.getName() != null && channel.getName().toLowerCase().contains(keyword.toLowerCase())) ||
                    (channel.getGroup() != null && channel.getGroup().toLowerCase().contains(keyword.toLowerCase())) ||
                    (channel.getCategory() != null && channel.getCategory().toLowerCase().contains(keyword.toLowerCase())) ||
                    (channel.getCountry() != null && channel.getCountry().toLowerCase().contains(keyword.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public void loadIptvData() {
        try {
            log.info("开始加载IPTV数据...");
            URL url = new URL(IPTV_URL);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String line;
                IptvChannel currentChannel = null;
                
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("#EXTINF:")) {
                        currentChannel = parseExtinfLine(line);
                    } else if (line.startsWith("http") && currentChannel != null) {
                        currentChannel.setUrl(line);
                        allChannels.add(currentChannel);
                        currentChannel = null;
                    }
                }
            }
            log.info("IPTV数据加载完成，共加载 {} 个频道", allChannels.size());
        } catch (Exception e) {
            log.error("加载IPTV数据失败: {}", e.getMessage(), e);
        }
    }

    private IptvChannel parseExtinfLine(String line) {
        IptvChannel channel = new IptvChannel();
        
        // 解析频道名称
        Pattern namePattern = Pattern.compile(",(.*?)(?=\\s*$)");
        Matcher nameMatcher = namePattern.matcher(line);
        if (nameMatcher.find()) {
            channel.setName(nameMatcher.group(1).trim());
        }

        // 解析分组信息
        Pattern groupPattern = Pattern.compile("group-title=\"([^\"]*)\"");
        Matcher groupMatcher = groupPattern.matcher(line);
        if (groupMatcher.find()) {
            channel.setGroup(groupMatcher.group(1));
            // 根据分组信息设置分类
            setCategoryFromGroup(channel);
        }

        // 解析语言信息
        Pattern languagePattern = Pattern.compile("language=\"([^\"]*)\"");
        Matcher languageMatcher = languagePattern.matcher(line);
        if (languageMatcher.find()) {
            channel.setLanguage(languageMatcher.group(1));
        }

        // 解析国家信息
        Pattern countryPattern = Pattern.compile("country=\"([^\"]*)\"");
        Matcher countryMatcher = countryPattern.matcher(line);
        if (countryMatcher.find()) {
            channel.setCountry(countryMatcher.group(1));
        }

        // 解析logo
        Pattern logoPattern = Pattern.compile("tvg-logo=\"([^\"]*)\"");
        Matcher logoMatcher = logoPattern.matcher(line);
        if (logoMatcher.find()) {
            channel.setLogo(logoMatcher.group(1));
        }

        return channel;
    }

    private void setCategoryFromGroup(IptvChannel channel) {
        String group = channel.getGroup().toLowerCase();
        
        if (group.contains("movie") || group.contains("cinema") || group.contains("film")) {
            channel.setCategory("电影");
        } else if (group.contains("music") || group.contains("musica")) {
            channel.setCategory("音乐");
        } else if (group.contains("sport") || group.contains("sports")) {
            channel.setCategory("体育");
        } else if (group.contains("news") || group.contains("新闻")) {
            channel.setCategory("新闻");
        } else if (group.contains("entertainment") || group.contains("娱乐")) {
            channel.setCategory("娱乐");
        } else if (group.contains("education") || group.contains("教育")) {
            channel.setCategory("教育");
        } else if (group.contains("kids") || group.contains("children") || group.contains("少儿")) {
            channel.setCategory("少儿");
        } else if (group.contains("documentary") || group.contains("纪录片")) {
            channel.setCategory("纪录片");
        } else if (group.contains("chinese") || group.contains("china") || group.contains("中文") || group.contains("中国")) {
            channel.setCategory("中文频道");
        } else if (group.contains("english") || group.contains("uk") || group.contains("us") || group.contains("美国") || group.contains("英国")) {
            channel.setCategory("英文频道");
        } else {
            channel.setCategory("其他");
        }
    }
}