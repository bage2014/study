package com.bage.study.best.practice.biz.steps.order.place;

import com.bage.study.best.practice.biz.steps.AbstractOrderStepHandler;

import java.util.Random;

public class OrderStepOrderAddHandler extends AbstractOrderStepHandler {

    @Override
    protected Boolean process(Object context) {
        // 订单流程处理
        boolean result = "mock".equals(context) || (new Random().nextInt(100)) > 1;
        System.out.println(this.getClass().getSimpleName() + "-executed-" + result);
        return result;
    }
}
