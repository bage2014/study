package com.bage.my.app.end.point.service.impl;

import com.bage.my.app.end.point.entity.AppLike;
import com.bage.my.app.end.point.entity.IptvChannel;
import com.bage.my.app.end.point.model.response.CategoryChannelsResponse;
import com.bage.my.app.end.point.model.response.Channel;
import com.bage.my.app.end.point.model.response.ChannelQueryResponse;
import com.bage.my.app.end.point.repository.IptvChannelRepository;
import com.bage.my.app.end.point.service.AppLikeService;
import com.bage.my.app.end.point.service.IptvService;
import com.bage.my.app.end.point.util.AuthUtil;
import com.bage.my.app.end.point.util.JsonUtil;
import com.bage.my.app.end.point.model.request.SearchRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    
    @Autowired
    private IptvChannelRepository channelRepository;
    
    @Autowired
    private AppLikeService appLikeService;

    private final String IPTV_URL = "https://iptv-org.github.io/iptv/index.m3u";

    @Override
    public List<IptvChannel> getAllChannels() {
        // 先从数据库查询
        List<IptvChannel> channels = channelRepository.findAll();
        
        // 如果数据库为空，则加载数据
        if (channels.isEmpty()) {
            loadIptvData();
            channels = channelRepository.findAll();
        }
        
        return channels;
    }

    @Override
    public ChannelQueryResponse getChannels(List<String> tags) {
        List<IptvChannel> channels = getAllChannels();
        if (tags == null || tags.isEmpty()) {
            return new ChannelQueryResponse(convertToChannelList(channels));
        }

        List<IptvChannel> filteredChannels = channels;
        for (String tag : tags) {
            if (tag == null || tag.isEmpty()) {
                continue;
            }
            log.info("过滤标签: {}", tag);
            filteredChannels = filteredChannels.stream()
                .filter(channel -> channel.getTags() != null && channel.getTags().contains(tag))
                .collect(Collectors.toList());
        }

        return new ChannelQueryResponse(convertToChannelList(filteredChannels));
    }
    
    /**
     * 将IptvChannel列表转换为Channel列表，并设置isLike字段
     */
    private List<Channel> convertToChannelList(List<IptvChannel> iptvChannels) {
        if (iptvChannels == null || iptvChannels.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取当前用户ID
        Long userId = AuthUtil.getCurrentUserId();
        
        // 如果用户未登录，则所有频道isLike=false
        if (userId == null) {
            return iptvChannels.stream()
                    .map(com.bage.my.app.end.point.model.response.Channel::new)
                    .collect(Collectors.toList());
        }
        
        // 查询用户喜欢的所有频道ID
        List<Long> likedChannelIds = appLikeService.findAllByUserId(userId)
                .stream()
                .map(appLike -> appLike.getRefId())
                .collect(Collectors.toList());
        
        // 转换为Channel对象并设置isLike字段
        return iptvChannels.stream()
                .map(iptvChannel -> {
                    Channel channel = new Channel(iptvChannel);
                    // 设置isLike字段
                    channel.setIsLike(likedChannelIds.contains(iptvChannel.getId()));
                    return channel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void loadIptvData() {
        try {
            log.info("开始加载IPTV数据...");
            URL url = new URL(IPTV_URL);
            List<IptvChannel> channelsToSave = new ArrayList<>();
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String line;
                IptvChannel currentChannel = null;
                long channelId = 1; // ID计数器
                
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("#EXTINF:")) {
                        currentChannel = parseExtinfLine(line);
                        currentChannel.setId(channelId++); // 设置ID
                    } else if (line.startsWith("http") && currentChannel != null) {
                        currentChannel.setUrl(line);
                        channelsToSave.add(currentChannel);
                        currentChannel.addTag("iptv"); // 添加默认标签
                        currentChannel = null;
                    }
                }
            }
            
            // 保存到数据库
            channelRepository.saveAll(channelsToSave);
            log.info("IPTV数据加载完成，共加载并保存 {} 个频道到数据库", channelsToSave.size());
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
            channel.addTag(channel.getName()); // 添加名称标签  
        }

        // 解析分组信息
        Pattern groupPattern = Pattern.compile("group-title=\"([^\"]*)\"");
        Matcher groupMatcher = groupPattern.matcher(line);
        if (groupMatcher.find()) {
            channel.setGroup(groupMatcher.group(1));
            // 根据分组信息设置分类
            setCategoryFromGroup(channel);
            channel.addTag(channel.getGroup()); // 添加分组标签
            channel.addTag(channel.getCategory()); // 添加分类标签
        }

        // 解析语言信息
        Pattern languagePattern = Pattern.compile("language=\"([^\"]*)\"");
        Matcher languageMatcher = languagePattern.matcher(line);
        if (languageMatcher.find()) {
            channel.setLanguage(languageMatcher.group(1));
            channel.addTag(channel.getLanguage()); // 添加语言标签
        }

        // 解析国家信息
        Pattern countryPattern = Pattern.compile("country=\"([^\"]*)\"");
        Matcher countryMatcher = countryPattern.matcher(line);
        if (countryMatcher.find()) {
            channel.setCountry(countryMatcher.group(1));
            channel.addTag(channel.getCountry()); // 添加国家标签
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

    @Override
    public Page<Channel> getChannelsByTagWithPagination(List<String> tags, Pageable pageable) {
        List<IptvChannel> channels = getAllChannels();
        List<IptvChannel> filteredChannels = channels;
        
        // 应用标签过滤
        if (tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                if (tag != null && !tag.isEmpty()) {
                    log.info("过滤标签: {}", tag);
                    filteredChannels = filteredChannels.stream()
                        .filter(channel -> channel.getTags() != null && channel.getTags().contains(tag))
                        .collect(Collectors.toList());
                }
            }
        }
        
        // 应用分页
        int start = Math.min((int) pageable.getOffset(), filteredChannels.size());
        int end = Math.min((start + pageable.getPageSize()), filteredChannels.size());
        
        List<IptvChannel> pagedChannels = filteredChannels.subList(start, end);
        
        // 将IptvChannel转换为Channel对象
        List<Channel> pagedChannelResponses = convertToChannelList(pagedChannels);
        
        return new PageImpl<>(pagedChannelResponses, pageable, filteredChannels.size());
    }

    @Override
    public Map<String, List<IptvChannel>> getChannelsByGroup(String keyword) {
        List<IptvChannel> channels = getAllChannels();
        log.info("获取所有频道数量: {}", channels.size());
        // 根据关键词过滤频道
        if (keyword != null && !keyword.isEmpty()) {
            channels = channels.stream()
                .filter(channel -> {
                    // 检查频道分类中是否包含关键词
                    return (channel.getCategory() != null && channel.getCategory().contains(keyword));
                })
                .collect(Collectors.toList());
            log.info("根据关键词过滤后的频道数量: {}", channels.size());
        }
        
        // 按category字段分组过滤后的频道数据
        return channels.stream()
            .filter(channel -> channel.getCategory() != null && !channel.getCategory().isEmpty())
            .collect(Collectors.groupingBy(IptvChannel::getCategory));
    }
    

    @Override
    public void addFavoriteChannel(Long userId, Long channelId) {
        log.info("添加喜欢的频道: userId={}, channelId={}", userId, channelId);
        
        // 检查频道是否存在
        boolean channelExists = channelRepository.existsById(channelId);
        
        if (!channelExists) {
            throw new RuntimeException("频道不存在: " + channelId);
        }
        
        // 使用LikeService添加喜欢关系
        appLikeService.addLike(userId, channelId);
    }
    
    @Override
    public void removeFavoriteChannel(Long userId, Long channelId) {
        log.info("删除喜欢的频道: userId={}, channelId={}", userId, channelId);
        
        // 检查频道是否存在
        boolean channelExists = channelRepository.existsById(channelId);
        
        if (!channelExists) {
            throw new RuntimeException("频道不存在: " + channelId);
        }
        
        // 使用LikeService删除喜欢关系
        appLikeService.removeLike(userId, channelId);
    }
    
    @Override
    public Page<Channel> getFavoriteChannelsWithPagination(Long userId, SearchRequest request) {
        log.info("获取用户喜欢的频道(分页): userId={}, 请求参数: {}", userId, JsonUtil.toJson(request));
        
        if(request == null){
            request = new SearchRequest();
        }
        
        // 1. 查询用户喜欢的所有频道ID
        List<Long> favoriteChannelIds = appLikeService.findAllByUserId(userId)
                .stream()
                .map(AppLike::getRefId)
                .collect(Collectors.toList());
        
        log.info("用户喜欢的频道数量: {}", favoriteChannelIds.size());
        
        // 2. 如果没有喜欢的频道，返回空分页结果
        if (favoriteChannelIds.isEmpty()) {
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
            return new PageImpl<>(List.of(), pageable, 0);
        }
        
        // 3. 对喜欢的频道ID进行分页
        int page = request.getPage();
        int size = request.getSize();
        Pageable pageable = PageRequest.of(page, size);
        
        int start = Math.min((int) pageable.getOffset(), favoriteChannelIds.size());
        int end = Math.min((start + pageable.getPageSize()), favoriteChannelIds.size());
        
        List<Long> pagedChannelIds = favoriteChannelIds.subList(start, end);
        
        // 4. 根据分页后的ID查询频道信息
        List<IptvChannel> pagedChannels = channelRepository.findAllById(pagedChannelIds);
        
        // 5. 应用关键词过滤
        String keyword = request.getKeyword();
        if (keyword != null && !keyword.isEmpty()) {
            String lowerKeyword = keyword.toLowerCase();
            pagedChannels = pagedChannels.stream()
                    .filter(channel -> 
                        (channel.getName() != null && channel.getName().toLowerCase().contains(lowerKeyword)) ||
                        (channel.getCategory() != null && channel.getCategory().toLowerCase().contains(lowerKeyword)) ||
                        (channel.getTags() != null && channel.getTags().contains(lowerKeyword))
                    )
                    .collect(Collectors.toList());
        }
        
        // 6. 将IptvChannel映射为Channel对象，并设置isLike=true
        List<Channel> pagedChannelResponses = pagedChannels.stream()
                .map(iptvChannel -> {
                    Channel channel = new Channel(iptvChannel);
                    channel.setIsLike(true); // 因为是喜欢的频道，所以isLike=true
                    return channel;
                })
                .collect(Collectors.toList());
        
        log.info("获取用户喜欢的频道(分页)成功: userId={}, 页码: {}, 每页数量: {}, 总数: {}, 返回数量: {}", 
                userId, page, size, favoriteChannelIds.size(), pagedChannelResponses.size());
        
        return new PageImpl<>(pagedChannelResponses, pageable, favoriteChannelIds.size());
    }
}