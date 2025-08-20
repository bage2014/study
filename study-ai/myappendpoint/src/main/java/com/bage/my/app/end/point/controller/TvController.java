package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.TvChannel;
import com.bage.my.app.end.point.service.TvService;
import com.bage.my.app.end.point.model.response.TvChannelListResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tv")
@Slf4j
public class TvController {

    private final TvService tvService;

    public TvController(TvService tvService) {
        this.tvService = tvService;
    }

    @GetMapping("/channels")
    public ResponseEntity<TvChannelListResponse> getAllTvChannels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TvChannel> tvChannelsPage = tvService.findAll(pageable);
        
        TvChannelListResponse response = new TvChannelListResponse(
                tvChannelsPage.getContent(),
                tvChannelsPage.getTotalElements(),
                tvChannelsPage.getTotalPages(),
                tvChannelsPage.getNumber(),
                tvChannelsPage.getSize()
        );
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<TvChannelListResponse> searchTvChannels(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("searchTvChannels keyword={}", keyword);
        Pageable pageable = PageRequest.of(page, size);
        Page<TvChannel> tvChannelsPage = tvService.searchByKeyword(keyword, pageable);
        log.info("searchTvChannels tvChannels={}", tvChannelsPage);
        
        TvChannelListResponse response = new TvChannelListResponse(
                tvChannelsPage.getContent(),
                tvChannelsPage.getTotalElements(),
                tvChannelsPage.getTotalPages(),
                tvChannelsPage.getNumber(),
                tvChannelsPage.getSize()
        );
        
        return ResponseEntity.ok(response);
    }
}