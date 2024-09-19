package com.bage.study.lombok;

import lombok.experimental.Delegate;

/**
 * https://juejin.cn/post/7322724142779252762
 */
public class DelegateBService {
    @Delegate // 委托A类的方法
    private DelegateAService aService = new DelegateAService();

    public static void main(String[] args) {
        DelegateBService b = new DelegateBService();
        b.sayHello(); // 调用A类的方法
    }


}