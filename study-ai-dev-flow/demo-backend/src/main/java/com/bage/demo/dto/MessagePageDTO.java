package com.bage.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class MessagePageDTO {

    private List<MessageDTO> content;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;
}