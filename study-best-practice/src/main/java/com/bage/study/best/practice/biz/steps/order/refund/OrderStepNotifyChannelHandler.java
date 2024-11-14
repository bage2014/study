package com.bage.study.best.practice.biz.steps.order.refund;

import com.bage.study.best.practice.biz.model.BaseContext;
import com.bage.study.best.practice.biz.steps.AbstractOrderStepHandler;

import java.util.Random;

public class OrderStepNotifyChannelHandler extends AbstractOrderStepHandler<BaseContext> {

    @Override
    protected Boolean process(BaseContext context) {
        // 订单流程处理
        boolean result = "mock".equals(context) || (new Random().nextInt(100)) > 5;
        System.out.println(this.getClass().getSimpleName() + "-executed-" + result);
        return result;
    }
}
