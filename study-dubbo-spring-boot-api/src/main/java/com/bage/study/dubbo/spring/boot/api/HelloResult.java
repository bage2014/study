package com.bage.study.dubbo.spring.boot.api;

import java.io.Serializable;
import java.util.List;

public class HelloResult implements Serializable {

    private Integer code;
    private String message;
    private List<User> userList;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public String toString() {
        return "HelloResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", userList=" + userList +
                '}';
    }
}
