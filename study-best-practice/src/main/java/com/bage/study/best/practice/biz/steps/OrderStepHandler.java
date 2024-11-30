package com.bage.study.best.practice.biz.steps;

import java.util.List;

/**
 * 抽象的订单步骤处理过程
 * 不足：
 * 不好支持N个handler的整体事务提交处理！！！！
 * 对业务抽取单独逻辑，不太友好
 */
public interface OrderStepHandler<T> {

    Boolean match(T context);


    Boolean execute(T context);

    int addTransactionStep(Runnable runnable);

    int submit();
}
