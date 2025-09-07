package com.bage.my.app.end.point.enums;

public enum VerificationStatus {
    PENDING("PENDING", "待认证"),    // 待认证
    APPROVED("APPROVED", "已认证"),   // 已认证
    REJECTED("REJECTED", "已拒绝");    // 已拒绝
    
    private final String code;
    private final String desc;
    
    VerificationStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
}