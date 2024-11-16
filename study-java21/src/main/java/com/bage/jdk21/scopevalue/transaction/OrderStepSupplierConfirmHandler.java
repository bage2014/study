package com.bage.jdk21.scopevalue.transaction;

public class OrderStepSupplierConfirmHandler extends StepHandler<BaseContext> {
    OrderConfirmDomain2Service domain2Service = new OrderConfirmDomain2Service();
    @Override
    protected Boolean process(BaseContext context) {
        // 订单流程处理
        domain2Service.process();

        addTransactionStep(context,()->domain2Service.process());

        return true;
    }

}
