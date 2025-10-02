package com.bage.my.app.end.point.model.request;

import com.bage.my.app.end.point.dto.PageQueryRequest;

import lombok.Data;

@Data
public class SearchRequest extends PageQueryRequest {
    private String keyword;
}