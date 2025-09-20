package com.bage.my.app.end.point.model.response;

import com.bage.my.app.end.point.entity.Trajectory;
import java.util.List;

public class TrajectoryListResponse extends PageResponse {
    private List<Trajectory> trajectories;

    // 构造函数
    public TrajectoryListResponse(List<Trajectory> trajectories, long totalElements, int totalPages, int currentPage, int pageSize) {
        super(totalElements, totalPages, currentPage, pageSize);
        this.trajectories = trajectories;
    }

    // Getters and Setters
    public List<Trajectory> getTrajectories() { return trajectories; }
    public void setTrajectories(List<Trajectory> trajectories) { this.trajectories = trajectories; }
}