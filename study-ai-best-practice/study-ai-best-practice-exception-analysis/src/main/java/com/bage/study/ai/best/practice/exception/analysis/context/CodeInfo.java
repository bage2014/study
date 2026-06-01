package com.bage.study.ai.best.practice.exception.analysis.context;

import java.time.LocalDateTime;

public class CodeInfo {

    private String appId;
    private String repositoryUrl;
    private String branch;
    private String commitId;
    private LocalDateTime commitTime;
    private String commitMessage;
    private String author;
    private String changedFiles;
    private String recentChangesSummary;

    public CodeInfo() {
    }

    public CodeInfo(String appId, String repositoryUrl, String branch, String commitId,
                   LocalDateTime commitTime, String commitMessage, String author,
                   String changedFiles, String recentChangesSummary) {
        this.appId = appId;
        this.repositoryUrl = repositoryUrl;
        this.branch = branch;
        this.commitId = commitId;
        this.commitTime = commitTime;
        this.commitMessage = commitMessage;
        this.author = author;
        this.changedFiles = changedFiles;
        this.recentChangesSummary = recentChangesSummary;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public LocalDateTime getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(LocalDateTime commitTime) {
        this.commitTime = commitTime;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getChangedFiles() {
        return changedFiles;
    }

    public void setChangedFiles(String changedFiles) {
        this.changedFiles = changedFiles;
    }

    public String getRecentChangesSummary() {
        return recentChangesSummary;
    }

    public void setRecentChangesSummary(String recentChangesSummary) {
        this.recentChangesSummary = recentChangesSummary;
    }
}