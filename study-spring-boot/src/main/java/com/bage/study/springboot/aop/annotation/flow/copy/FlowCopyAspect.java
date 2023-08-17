package com.bage.study.springboot.aop.annotation.flow.copy;

import com.bage.study.springboot.spel.AopUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Component
@Aspect
public class FlowCopyAspect {

    private static final Logger log = LoggerFactory.getLogger(FlowCopyAspect.class);

    @Autowired
    private FlowCopyLogic flowCopyLogic;

    @Around(value = "@annotation(com.bage.study.springboot.aop.annotation.flow.copy.FlowCopy)")
    public Object withMethod(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = null;
        Object result = null;
        Method method = null;
        FlowCopy annotation = null;
        try {
            method = AopUtils.getMethod(pjp);
            args = AopUtils.getArgs(pjp);
            annotation = AopUtils.getMethodAnnotation(method, FlowCopy.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        // 执行对应方法
        Exception proceedException = null;
        try {
            result = pjp.proceed();
        } catch (Exception e) {
            proceedException = e;
        }

        try {
            if (Objects.nonNull(annotation)) {
                flowCopyLogic.doCopy(annotation, method, args, result, proceedException);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        // 不处理业务异常，直接上抛
        if (Objects.nonNull(proceedException)) {
            throw proceedException;
        }
        return result;
    }

}
