package com.bage.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageQueryDTO {

    private Long id;

    private String content;

    private String keyword;

    private int page = 1;

    private int size = 10;
}
