package com.bage.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for creating a message.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageCreateRequest {

    private String content;
    private String sender;
    private String receiver;
}
