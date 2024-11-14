package com.bage.study.best.practice.biz.steps.order.place;

import com.bage.study.best.practice.biz.model.BaseContext;
import com.bage.study.best.practice.biz.steps.AbstractOrderStepHandler;

import java.util.Random;

public class OrderStepSupplierPlaceHandler extends AbstractOrderStepHandler<BaseContext> {

    @Override
    protected Boolean process(BaseContext context) {
        // 订单流程处理
        boolean result = "mock".equals(context) || (new Random().nextInt(100)) > 100;
        System.out.println(this.getClass().getSimpleName() + "-executed-" + result);
        return result;
    }
}
