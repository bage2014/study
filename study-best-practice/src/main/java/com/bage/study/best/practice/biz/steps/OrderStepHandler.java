package com.bage.study.best.practice.biz.steps;

/**
 * 抽象的订单步骤处理过程
 */
public interface OrderStepHandler {

    Boolean execute(Object context);

}
