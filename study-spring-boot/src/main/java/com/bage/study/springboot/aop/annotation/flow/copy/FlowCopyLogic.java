package com.bage.study.springboot.aop.annotation.flow.copy;

import com.bage.study.springboot.aop.annotation.log.LoggerAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

public class FlowCopyLogic implements ApplicationContextAware {
    private ApplicationContext context;
    @Autowired
    private FlowCopyConfigService flowCopyConfigService;
    @Autowired
    @Qualifier("flowCopyThreadPoolExecutor")
    private ThreadPoolExecutor flowCopyThreadPoolExecutor;
    private Random random = new Random();
    private static final Logger log = LoggerFactory.getLogger(LoggerAspect.class);

    public void doCopy(FlowCopy annotation, Method method, Object[] args, Object response, Exception proceedException) {
        log.info("doCopy, annotation = {}", annotation.getClass().getSimpleName());
        Class copyToClass = annotation.toClass();
        // 查询配置
        Optional<FlowCopyConfig> first = getFlowCopyConfig(annotation, method);
        if (!first.isPresent()) {
            log.warn("can not get flow copy config from context, copyToClass = {}", copyToClass.getName());
            return;
        }
        if (method.getDeclaringClass() == copyToClass) {
            log.error("copyToClass config illegal, copyToClass = {}, method.class = {}", copyToClass, method.getDeclaringClass());
            return;
        }

        // 获取Bean 实例
        Map<String, Object> beansOfType = context.getBeansOfType(copyToClass);
        if (beansOfType == null || beansOfType.isEmpty()) {
            log.warn("can not get bean instance from context, copyToClass = {}", copyToClass.getName());
            return;
        }

        FlowCopyConfig config = first.get();
        int randomNumber = random.nextInt(config.getTotal());
        if (randomNumber >= config.getPercent()) {
            // 命中，比如配置 0/100，则表示关闭；比如生成 随机数 50； 配置 10/1000
            // 配置 10/1000 表示流量开启 百分之一
            log.info("doCopy return, randomNumber={},percent={},total={}", randomNumber, config.getPercent(), config.getTotal());
            return;
        }

        // 相当于 randomNumber < config.getPercent() ,比如生成 随机数 5； 配置 10/1000
        beansOfType.forEach((beanName, bean) -> {
            // 参数
            FlowCopyParam build = new FlowCopyParam();
            build.setAnnotation(annotation);
            build.setFromClass(method.getClass());
            build.setConfig(config);
            build.setArgs(args);
            build.setFromResponse(response);
            build.setFromMethod(method.getName());
            build.setToMethod("".equals(annotation.toMethod()) ? method.getName() : annotation.toMethod());
            build.setParameterTypes(method.getParameterTypes());
            build.setBeanName(beanName);
            build.setBean(bean);
            build.setProceedException(proceedException);
            // 提交流量复制操作
            submitFlowCopy(build);
        });

    }

    private void submitFlowCopy(FlowCopyParam param) {
        log.info("submitFlowCopy isSync = {}", param.getConfig().isSync());
        if (param.getConfig().isSync()) {
            submitFlowCopySync(param);
            return;
        }
        submitFlowCopyAsync(param);
    }

    private int submitFlowCopyAsync(FlowCopyParam param) {
        flowCopyThreadPoolExecutor.execute(() -> {
            submitFlowCopySync(param);
        });
        return 0;
    }

    private void submitFlowCopySync(FlowCopyParam param) {
        // 同步，直接执行
        Object invoke = null;
        Exception copyException = null;
        FlowCopyFinishParam build = new FlowCopyFinishParam();
        try {
            // build param
            build.setFromClass(param.getFromClass());
            build.setFromResponse(param.getFromResponse());
            build.setToClass(param.getAnnotation().toClass());
            build.setArgs(param.getArgs());
            build.setFromMethod(param.getFromMethod());
            build.setToMethod(param.getToMethod());

            Class copyToClass = param.getAnnotation().toClass();
            Method copyToMethod = copyToClass.getMethod(param.getToMethod(), param.getParameterTypes());
            invoke = copyToMethod.invoke(param.getBean(), param.getArgs());
            log.info("copyToMethod is invoked, beanName = {}", param.getBeanName());
            // build response
            build.setToResponse(invoke);
        } catch (Exception e) {
            copyException = e;
            log.error(e.getMessage(), e);
        }
        // post listener
        callListener(param, copyException, build);
    }

    private void callListener(FlowCopyParam param, Exception copyException, FlowCopyFinishParam build) {
        try {
            List<FlowCopyFinishListener> listenerList = param.getConfig().getListener();
            if (listenerList == null || listenerList.isEmpty()) {
                log.info("listenerList is null or empty, return ");
                return;
            }
            for (FlowCopyFinishListener listener : listenerList) {
                // 异常情况
                if (copyException != null || param.getProceedException() != null) {
                    // 正常进行
                    listener.onException(build, param.getProceedException(), copyException);
                    log.info("postService is worked, listener = {}", listener.getClass());
                    continue;
                }

                // 正常复制
                listener.onFinish(build);
                log.info("postService is worked, listener = {}", listener.getClass());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 1、 优先匹配 类名、方法名 完全一致的
     * 2、 否则， 匹配 类名一样，方法名是 * 或者 NULL 的
     * 3、 否则， 匹配 基类，方法名 完全一致的
     * 4、 否则， 匹配 基类，方法名是 * 或者 NULL 的
     *
     * @param annotation
     * @param method
     * @return
     */
    private Optional<FlowCopyConfig> getFlowCopyConfig(FlowCopy annotation, Method method) {
        Class toClass = annotation.toClass();
        String toMethod = "".equals(annotation.toMethod()) ? method.getName() : annotation.toMethod();
        // 优先匹配 类名、方法名 完全一致的
        Optional<FlowCopyConfig> first = flowCopyConfigService.getFlowCopyConfigList()
                .stream()
                .filter(item -> Objects.equals(item.getToClass(), toClass))
                .filter(item -> Objects.equals(item.getToMethod(), toMethod))
                .findFirst();
        // 否则， 匹配 类名一样，方法名是 * 或者 NULL 的
        if (!first.isPresent()) {
            first = flowCopyConfigService.getFlowCopyConfigList()
                    .stream()
                    .filter(item -> Objects.equals(item.getToClass(), toClass))
                    .filter(item -> nullOrAllMethod(item))
                    .findFirst();
        }

        // 否则， 匹配 基类，方法名一样
        // 比如 配置的是 Person ，流量复制目标类是 Man 或者 Women 都可以生效
        if (!first.isPresent()) {
            first = flowCopyConfigService.getFlowCopyConfigList()
                    .stream()
                    .filter(item -> toClass.isAssignableFrom(item.getToClass()))
                    .filter(item -> Objects.equals(item.getToMethod(), toMethod))
                    .findFirst();
        }

        // 否则， 匹配 基类，方法名是 * 或者 NULL 的
        if (!first.isPresent()) {
            first = flowCopyConfigService.getFlowCopyConfigList()
                    .stream()
                    .filter(item -> toClass.isAssignableFrom(item.getToClass()))
                    .filter(item -> nullOrAllMethod(item))
                    .findFirst();
        }

        return first;
    }

    private boolean nullOrAllMethod(FlowCopyConfig item) {
        return item.getToMethod() == null || Objects.equals(item.getToMethod().trim(), "*");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
