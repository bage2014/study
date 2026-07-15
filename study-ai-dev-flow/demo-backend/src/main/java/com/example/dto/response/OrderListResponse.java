package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponse {

    private List<OrderResponse> orders;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}
