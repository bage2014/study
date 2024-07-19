package com.bage.study.flowable;


/**
 * @ 审批列表查询结果
 */
public class TaskVO {
    private String id;
    private String day;
    private String name;

    public TaskVO(String id, String day, String name) {
        this.id = id;
        this.day = day;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TaskVO{" +
                "id='" + id + '\'' +
                ", day='" + day + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
