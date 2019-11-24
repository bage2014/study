package com.bage.study.springboot.check;

public class ValidateResult {

    /**
     * 校验是否通过
     */
    boolean isOk;

    /**
     * 校验结果说明
     */
    String msg;

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
