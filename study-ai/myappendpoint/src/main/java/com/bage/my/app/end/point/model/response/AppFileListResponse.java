package com.bage.my.app.end.point.model.response;

import com.bage.my.app.end.point.entity.AppFile;
import lombok.Data;
import java.util.List;

@Data
public class AppFileListResponse extends PageResponse {
    private List<AppFile> files;
    
    public AppFileListResponse(List<AppFile> files, long totalElements, int totalPages, int currentPage, int pageSize) {
        super(totalElements, totalPages, currentPage, pageSize);
        this.files = files;
    }
}