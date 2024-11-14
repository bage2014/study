package com.bage.jdk21.scopevalue.transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionsFlowStrategy implements FlowStrategy<Object> {

    public static void main(String[] args) {
        BaseContext context = new BaseContext();
        ScopedValue.where(context.getTransactionScopeValueList(), new ArrayList<>()).run(() -> {
            OrderStepOrderConfirmHandler start = new OrderStepOrderConfirmHandler();
            start.next(new OrderStepSupplierConfirmHandler())
            ;
            start.execute(context);

            List<Runnable> list = context.getTransactionScopeValueList().get();
            System.out.println("getTransactionScopeValueList.size = " + list.size());
            for (Runnable runnable : list) {
                runnable.run();
            }
        });
    }
}
