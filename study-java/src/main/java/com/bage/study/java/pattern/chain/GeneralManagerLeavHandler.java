package com.bage.study.java.pattern.chain;

import java.util.Random;

public class GeneralManagerLeavHandler extends LeaveHandler{
    public GeneralManagerLeavHandler(String name) {
        super(name);
    }
    @Override
    public boolean process(LeaveRequest leaveRequest) {
        // 随机数大于3则为批准，否则不批准
        boolean result = (new Random().nextInt(10)) > 3;
        if (!result) {
            System.out.println(this.getHandlerName() + "审批驳回");
            return false;
        } else {
            System.out.println(this.getHandlerName() + "审批完成");
            return true;
        }
    }
}
