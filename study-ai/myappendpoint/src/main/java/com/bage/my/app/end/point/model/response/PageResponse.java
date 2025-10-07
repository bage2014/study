package com.bage.my.app.end.point.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页响应
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse {
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

}