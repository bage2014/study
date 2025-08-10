package com.bage.my.app.end.point.model.response;

import com.bage.my.app.end.point.dto.MessageItem;
import java.util.List;

public class MessageListResponse {
    private List<MessageItem> messages;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    // 构造函数
    public MessageListResponse(List<MessageItem> messages, long totalElements, int totalPages, int currentPage, int pageSize) {
        this.messages = messages;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    // Getters and Setters
    public List<MessageItem> getMessages() { return messages; }
    public void setMessages(List<MessageItem> messages) { this.messages = messages; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}