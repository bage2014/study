package com.bage.my.app.end.point.dto;

import com.bage.my.app.end.point.entity.FamilyMember;
import java.util.List;

public class QueryFamilyResponse {
    private List<FamilyMember> members;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public QueryFamilyResponse() {}

    public QueryFamilyResponse(List<FamilyMember> members, long totalElements, int totalPages, int currentPage, int pageSize) {
        this.members = members;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    // Getter和Setter方法
    public List<FamilyMember> getMembers() { return members; }
    public void setMembers(List<FamilyMember> members) { this.members = members; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}