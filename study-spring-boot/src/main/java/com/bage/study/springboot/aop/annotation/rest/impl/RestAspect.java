package com.bage.study.springboot.aop.annotation.rest.impl;

import com.bage.study.springboot.aop.annotation.BaseAspect;
import com.bage.study.springboot.aop.annotation.rest.RestResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class RestAspect extends BaseAspect {

    private static final Logger log = LoggerFactory.getLogger(RestAspect.class);

    @Around(value = "@annotation(com.bage.study.springboot.aop.annotation.rest.RestResult)")
    public Object withMethod(ProceedingJoinPoint pjp) {

        // 获取参数
        // Object[] args = getArgs(pjp);
        // 目标类
        // Object target = getTarget(pjp);
        // 当前方法
        Method method = getMethod(pjp);
        // 代理对象
        // Object proxy = getProxy(pjp);
        // 注解
        RestResult annotation = method.getAnnotation(RestResult.class);

        Object result = null;
        if (annotation != null) {
            if (log.isDebugEnabled()) {
                log.debug("annotation: {}", annotation);
            }
            // start stopwatch
            try {
                result = pjp.proceed();

                if(result instanceof RestReresponse){
                    // do nothing
                } else {
                    RestReresponse restReresponse = new RestReresponse();
                    restReresponse.setCode(200);
                    restReresponse.setData(result);
                    if (log.isDebugEnabled()) {
                        log.debug("restReresponse: {}", restReresponse);
                    }
                    return restReresponse;
                }

            } catch (Throwable throwable) {
            }
            // stop stopwatch
        }

        return result;
    }

}


