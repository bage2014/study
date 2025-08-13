package com.bage.my.app.end.point.service;

import com.bage.my.app.end.point.entity.TvChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TvService {
    Page<TvChannel> findAll(Pageable pageable);
    Page<TvChannel> searchByKeyword(String keyword, Pageable pageable);
    void loadDataFromFile();
}