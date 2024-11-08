package com.bage.study.java.pattern.chain;

public class LeaveRequest {
    /**
     * 请假的人
     */
    private String name;

    /**
     * 请假的天数
     */
    private int days;

    public LeaveRequest() {
    }

    public LeaveRequest(String name, int days) {
        this.name = name;
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
