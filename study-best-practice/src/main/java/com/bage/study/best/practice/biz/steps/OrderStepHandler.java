package com.bage.study.best.practice.biz.steps;

/**
 * 抽象的订单步骤处理过程
 * 不足：
 * 不好支持N个handler的整体事务提交处理！！！！
 * 对业务抽取单独逻辑，不太友好
 */
public interface OrderStepHandler<T> {

    Boolean match(T context);


    Boolean execute(T context);

    default int transactionStep(T context){
        return 0;
    }


}
