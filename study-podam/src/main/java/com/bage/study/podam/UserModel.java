package com.bage.study.podam;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private Long id;
    private String userName;
    private List<UserModel> friends;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<UserModel> getFriends() {
        return friends;
    }

    public void setFriends(List<UserModel> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", friends=" + friends +
                '}';
    }
}
