package com.bage.my.app.end.point.model.response;

import com.bage.my.app.end.point.dto.MessageItem;
import java.util.List;

public class MessageListResponse extends PageResponse {
    private List<MessageItem> messages;

    // 构造函数
    public MessageListResponse(List<MessageItem> messages, long totalElements, int totalPages, int currentPage, int pageSize) {
        super(totalElements, totalPages, currentPage, pageSize);
        this.messages = messages;
    }

    // Getters and Setters
    public List<MessageItem> getMessages() { return messages; }
    public void setMessages(List<MessageItem> messages) { this.messages = messages; }
}