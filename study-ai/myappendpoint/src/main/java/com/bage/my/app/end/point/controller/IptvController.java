package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.IptvChannel;
import com.bage.my.app.end.point.service.IptvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/iptv")
@Slf4j
public class IptvController {

    private final IptvService iptvService;

    public IptvController(IptvService iptvService) {
        this.iptvService = iptvService;
    }

    @GetMapping("/channels")
    public ResponseEntity<List<IptvChannel>> getAllChannels() {
        try {
            List<IptvChannel> channels = iptvService.getAllChannels();
            return ResponseEntity.ok(channels);
        } catch (Exception e) {
            log.error("获取所有频道失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<Map<String, List<IptvChannel>>> getChannelsByCategory() {
        try {
            Map<String, List<IptvChannel>> categorizedChannels = iptvService.getChannelsByCategory();
            return ResponseEntity.ok(categorizedChannels);
        } catch (Exception e) {
            log.error("按分类获取频道失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<List<IptvChannel>> getChannelsByLanguage(@PathVariable String language) {
        try {
            List<IptvChannel> channels = iptvService.getChannelsByLanguage(language);
            return ResponseEntity.ok(channels);
        } catch (Exception e) {
            log.error("按语言获取频道失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<IptvChannel>> searchChannels(@RequestParam String keyword) {
        try {
            List<IptvChannel> channels = iptvService.searchChannels(keyword);
            return ResponseEntity.ok(channels);
        } catch (Exception e) {
            log.error("搜索频道失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/chinese")
    public ResponseEntity<List<IptvChannel>> getChineseChannels() {
        try {
            List<IptvChannel> chineseChannels = iptvService.getChannelsByLanguage("chinese")
                    .stream()
                    .filter(channel -> channel.getCategory() != null && 
                            (channel.getCategory().equals("中文频道") || 
                             channel.getCategory().equals("电影") ||
                             channel.getCategory().equals("音乐") ||
                             channel.getCategory().equals("新闻") ||
                             channel.getCategory().equals("娱乐")))
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(chineseChannels);
        } catch (Exception e) {
            log.error("获取中文频道失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/english")
    public ResponseEntity<List<IptvChannel>> getEnglishChannels() {
        try {
            List<IptvChannel> englishChannels = iptvService.getChannelsByLanguage("english")
                    .stream()
                    .filter(channel -> channel.getCategory() != null && 
                            (channel.getCategory().equals("英文频道") || 
                             channel.getCategory().equals("Movie") ||
                             channel.getCategory().equals("Music") ||
                             channel.getCategory().equals("News") ||
                             channel.getCategory().equals("Entertainment")))
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(englishChannels);
        } catch (Exception e) {
            log.error("获取英文频道失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/reload")
    public ResponseEntity<String> reloadData() {
        try {
            iptvService.loadIptvData();
            return ResponseEntity.ok("数据重新加载成功");
        } catch (Exception e) {
            log.error("重新加载数据失败: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("重新加载数据失败");
        }
    }
}