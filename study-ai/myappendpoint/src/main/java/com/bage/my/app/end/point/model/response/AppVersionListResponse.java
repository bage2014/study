package com.bage.my.app.end.point.model.response;

import com.bage.my.app.end.point.entity.AppVersion;
import java.util.List;

public class AppVersionListResponse extends PageResponse {
    private List<AppVersion> versions;

    // 构造函数
    public AppVersionListResponse(List<AppVersion> versions, long totalElements, int totalPages, int currentPage, int pageSize) {
        super(totalElements, totalPages, currentPage, pageSize);
        this.versions = versions;
    }

    // Getters and Setters
    public List<AppVersion> getVersions() { return versions; }
    public void setVersions(List<AppVersion> versions) { this.versions = versions; }
}