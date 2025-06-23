package com.example.demo.controller;

import com.example.demo.entity.M3uEntry;
import com.example.demo.util.M3uParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import jakarta.annotation.PostConstruct;

@RestController
public class M3uController {
    private List<M3uEntry> m3uEntries = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
        m3uEntries = M3uParser.parse("m3u/index.m3u");
        m3uEntries.get(0).setUrl("https://stream-akamai.castr.com/5b9352dbda7b8c769937e459/live_2361c920455111ea85db6911fe397b9e/index.fmp4.m3u8");
    }

    @GetMapping("/m3u/query")
    public Page<M3uEntry> parseM3u(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword
    ) {
        List<M3uEntry> filteredEntries = m3uEntries;
        if (!keyword.isEmpty()) {
            filteredEntries = m3uEntries.stream()
                   .filter(entry -> entry.getTitle().contains(keyword))
                   .collect(Collectors.toList());
        }
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredEntries.size());
        if (start > end) {
            start = 0;
            end = 0;
        }
        List<M3uEntry> pageContent = filteredEntries.subList(start, end);
        return new PageImpl<>(pageContent, pageable, filteredEntries.size());
    }
}