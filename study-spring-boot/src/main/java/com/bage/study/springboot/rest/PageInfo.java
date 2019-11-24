package com.bage.study.springboot.rest;

public class PageInfo {

    /**
     * 目标页
     */
    private int targetPage;
    /**
     * 页面条目
     */
    private int pageSize;
    /**
     * 总条目
     */
    private int total;

    public int getTargetPage() {
        return targetPage;
    }

    public void setTargetPage(int targetPage) {
        this.targetPage = targetPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
