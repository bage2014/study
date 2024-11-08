package com.bage.study.java.pattern.chain;

import java.util.Random;

/**
 * 天数小于3天, 直属领导处理
 */
public class DirectLeaveHandler extends LeaveHandler{
    public DirectLeaveHandler(String directName) {
        super(directName);
    }
    @Override
    public boolean process(LeaveRequest leaveRequest) {
        // 随机数大于3则为批准，否则不批准
        boolean result = (new Random().nextInt(10)) > 3;
        if (!result) {
            System.out.println(this.getHandlerName() + "审批驳回");
            return false;
        } else if (leaveRequest.getDays() <= 3) {
            // 审批通过
            System.out.println(this.getHandlerName() + "审批完成");
            return true;
        } else{
            System.out.println(this.getHandlerName() + "审批完成");
            return this.getNextHandler().process(leaveRequest);
        }
    }
}
