package com.bage.my.app.end.point.service.impl;

import com.bage.my.app.end.point.entity.TvChannel;
import com.bage.my.app.end.point.service.TvService;
import com.bage.my.app.end.point.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

import jakarta.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TvServiceImpl implements TvService {

    private List<TvChannel> tvChannels = new ArrayList<>();

    @PostConstruct
    @Override
    public void loadDataFromFile() {
        try {
            ClassPathResource resource = new ClassPathResource("m3u/all.json");
            String json = new String(resource.getInputStream().readAllBytes());
            List<TvChannel> channels = JsonUtil.fromJsonList(json, new TypeToken<List<TvChannel>>() {}.getType());
            for (TvChannel channel : channels) {
                tvChannels.add(channel);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load TV data from file", e);
        }
    }

    @Override
    public Page<TvChannel> findAll(Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), tvChannels.size());
        List<TvChannel> pageContent = tvChannels.subList(start, end);
        return new PageImpl<>(pageContent, pageable, tvChannels.size());
    }

    @Override
    public Page<TvChannel> searchByKeyword(String keyword, Pageable pageable) {
        List<TvChannel> filteredChannels = tvChannels.stream()
                .filter(channel -> channel.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredChannels.size());
        List<TvChannel> pageContent = filteredChannels.subList(start, end);
        return new PageImpl<>(pageContent, pageable, filteredChannels.size());
    }
}