package com.bage.study.best.practice.biz.steps;

/**
 * 抽象的订单步骤处理过程
 */
public interface OrderStepHandler<T> {

    Boolean match(T context);


    Boolean execute(T context);

    default int transactionStep(T context){
        return 0;
    }


}
