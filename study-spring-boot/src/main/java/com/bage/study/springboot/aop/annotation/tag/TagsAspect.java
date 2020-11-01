package com.bage.study.springboot.aop.annotation.tag;

import com.bage.study.springboot.spel.AopUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
public class TagsAspect {

    private static final Logger log = LoggerFactory.getLogger(TagsAspect.class);

    @Around(value = "@annotation(com.bage.study.springboot.aop.annotation.tag.Tags)")
    public Object withMethod(ProceedingJoinPoint pjp) throws NoSuchMethodException {

        Object[] args = AopUtils.getArgs(pjp);
        Method method = AopUtils.getMethod(pjp);
        Tags annotation = method.getAnnotation(Tags.class);

        Object result = null;
        log.info("param: {}", args);

        if (Objects.nonNull(annotation)) {
            if (log.isDebugEnabled()) {
                log.debug("annotation: {}", annotation);
            }
            // start stopwatch
            try {
                String traceId = annotation.traceId();
                log.info("traceId: {}", traceId);

                result = pjp.proceed();


            } catch (Throwable throwable) {
            }
            // stop stopwatch
        }
        log.info("result: {}", result);

        return result;
    }

}


