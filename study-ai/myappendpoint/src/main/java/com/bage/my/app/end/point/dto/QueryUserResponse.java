package com.bage.my.app.end.point.dto;

import com.bage.my.app.end.point.entity.User;
import org.springframework.data.domain.Page;

public class QueryUserResponse {
    private Page<User> users;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public QueryUserResponse() {}

    public QueryUserResponse(Page<User> users) {
        this.users = users;
        this.totalElements = users.getTotalElements();
        this.totalPages = users.getTotalPages();
        this.currentPage = users.getNumber();
        this.pageSize = users.getSize();
    }

    // Getter和Setter方法
    public Page<User> getUsers() { return users; }
    public void setUsers(Page<User> users) { this.users = users; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}