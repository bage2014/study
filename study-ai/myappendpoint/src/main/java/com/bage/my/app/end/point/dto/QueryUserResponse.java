package com.bage.my.app.end.point.dto;

import com.bage.my.app.end.point.entity.User;
import java.util.List;

public class QueryUserResponse {
    private List<User> users;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public QueryUserResponse() {}

    public QueryUserResponse(List<User> users, long totalElements, int totalPages, int currentPage, int pageSize) {
        this.users = users;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    // Getter和Setter方法
    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}