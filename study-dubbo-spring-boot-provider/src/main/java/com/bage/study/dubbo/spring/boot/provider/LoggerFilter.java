package com.bage.study.dubbo.spring.boot.provider;

import org.apache.dubbo.rpc.*;

public class LoggerFilter implements Filter {
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // before filter ...
        Result result = invoker.invoke(invocation);
        // after filter ...
        System.out.println("result = " + result.getValue());
        return result;
    }
}