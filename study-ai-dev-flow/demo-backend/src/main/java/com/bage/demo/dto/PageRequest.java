package com.bage.demo.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequest {

    @Min(value = 0, message = "页码必须大于等于0")
    private int page = 0;

    @Min(value = 1, message = "每页大小必须大于等于1")
    private int size = 10;

    private String sortBy = "createdAt";

    private String sortDirection = "desc";
}