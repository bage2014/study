package com.bage.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageQueryRequest {

    private String sender;
    private String receiver;
    private String messageType;
    private int page;
    private int size;
}
