package com.bage.domain;

import java.util.List;

public class ListList {

    List<List<String>> userss;

    public List<List<String>> getUserss() {
        return userss;
    }

    public void setUserss(List<List<String>> userss) {
        this.userss = userss;
    }

    @Override
    public String toString() {
        return "ListList{" +
                "userss=" + userss +
                '}';
    }
}
