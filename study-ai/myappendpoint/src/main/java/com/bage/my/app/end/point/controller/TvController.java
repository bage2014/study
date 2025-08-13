package com.bage.my.app.end.point.controller;

import com.bage.my.app.end.point.entity.TvChannel;
import com.bage.my.app.end.point.service.TvService;
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
public class TvController {

    private final TvService tvService;

    public TvController(TvService tvService) {
        this.tvService = tvService;
    }

    @GetMapping
    public ResponseEntity<Page<TvChannel>> getAllTvChannels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TvChannel> tvChannels = tvService.findAll(pageable);
        return ResponseEntity.ok(tvChannels);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<TvChannel>> searchTvChannels(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TvChannel> tvChannels = tvService.searchByKeyword(keyword, pageable);
        return ResponseEntity.ok(tvChannels);
    }
}