package com.example.model;

public enum OrderStatus {
    PENDING_PAYMENT("待支付"),
    PAID("已支付"),
    SHIPPED("已发货"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 校验状态变更是否合法
     * @param current 当前状态
     * @param target 目标状态
     * @return true if transition is valid
     */
    public static boolean isValidTransition(OrderStatus current, OrderStatus target) {
        if (current == null || target == null) {
            return false;
        }
        // 相同状态不允许变更
        if (current == target) {
            return false;
        }
        switch (current) {
            case PENDING_PAYMENT:
                // 待支付 -> 已支付 或 已取消
                return target == PAID || target == CANCELLED;
            case PAID:
                // 已支付 -> 已发货 或 已取消
                return target == SHIPPED || target == CANCELLED;
            case SHIPPED:
                // 已发货 -> 已完成 或 已取消
                return target == COMPLETED || target == CANCELLED;
            case COMPLETED:
                // 已完成不可变
                return false;
            case CANCELLED:
                // 已取消不可变
                return false;
            default:
                return false;
        }
    }
}