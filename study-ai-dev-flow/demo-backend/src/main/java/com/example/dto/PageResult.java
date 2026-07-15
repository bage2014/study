package com.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "分页结果")
public class PageResult<T> {

    @Schema(description = "数据列表")
    private List<T> content;

    @Schema(description = "当前页码", example = "0")
    private int page;

    @Schema(description = "每页大小", example = "20")
    private int size;

    @Schema(description = "总记录数", example = "100")
    private long totalElements;

    @Schema(description = "总页数", example = "5")
    private int totalPages;

    @Schema(description = "是否最后一页", example = "false")
    private boolean last;

    @Schema(description = "是否第一页", example = "true")
    private boolean first;
}