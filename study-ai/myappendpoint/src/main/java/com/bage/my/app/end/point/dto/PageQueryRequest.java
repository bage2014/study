package com.bage.my.app.end.point.dto;

import lombok.Data;

@Data
public class PageQueryRequest {
    private int page = 0;
    private int size = 10;
}