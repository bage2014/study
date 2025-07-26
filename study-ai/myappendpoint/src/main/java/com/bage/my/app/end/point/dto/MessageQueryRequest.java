package com.bage.my.app.end.point.dto;

import lombok.Data;

@Data
public class MessageQueryRequest {
    private Long receiverId;
    private Boolean isRead;
    private String startTime;
    private String endTime;
    private int page = 1;
    private int size = 10;
}
