package com.bage.study.best.practice.biz.steps.order.place;

import com.bage.study.best.practice.biz.domain.ProductDomainService;
import com.bage.study.best.practice.biz.model.BaseContext;
import com.bage.study.best.practice.biz.steps.AbstractOrderStepHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class OrderStepProductAddHandler extends AbstractOrderStepHandler<BaseContext> {

    @Autowired
    private ProductDomainService productDomainService;

    @Override
    protected Boolean process(BaseContext context) {
        // 订单流程处理
        boolean result = "mock".equals(context) || (new Random().nextInt(100)) > 100;
        System.out.println(this.getClass().getSimpleName() + "-executed-" + result);
        if (result) {
            productDomainService.add(context);
        }
        addTransactionStep(() -> {
            System.out.println("OrderStepProductAddHandler DB operation...");
        });
        return result;
    }

    @Override
    public Boolean match(BaseContext context) {
        return super.match(context) && hasProduct(context);
    }

    private boolean hasProduct(Object context) {
        return false;
    }
}
