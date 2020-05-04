package com.bage.study.springboot.restresponse;

public class RestReresponse {

    private int code;
    private String msg;
    private Object data;
    private Object bundle;

    public RestReresponse(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getBundle() {
        return bundle;
    }

    public void setBundle(Object bundle) {
        this.bundle = bundle;
    }
}
