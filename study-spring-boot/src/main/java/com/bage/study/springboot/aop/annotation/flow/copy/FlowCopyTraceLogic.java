package com.bage.study.springboot.aop.annotation.flow.copy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FlowCopyTraceLogic {

    private Map<Class, List<Class>> map = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FlowCopyTraceLogic.class);

    /**
     * @param fromClass
     * @param toClass
     * @return
     */
    public boolean check(Class fromClass, Class toClass) {
        // 表示 fromClass 到 fromList 每个节点 都有直接 流量复制关系
        List<Class> fromList = putIfNotPresentAndGet(fromClass, new ArrayList<>());
        putIfNotPresentAndGet(toClass, new ArrayList<>());
        // 流量复制链
        List<Class> traceList = new ArrayList<>();
        // 判断是否有直接 或者 间接关系
        boolean isContains = ifTraceContains(toClass, fromClass, traceList);
        if (isContains) {
            String collect = traceList.stream().map(item -> item.getSimpleName()).collect(Collectors.joining("->"));
            log.info("can not copy to an exist flow, cause exist call: {}", collect);
            return false;
        }

        // 添加到直接关系
        fromList.add(toClass);
        return true;
    }

    private boolean ifTraceContains(Class toClass, Class fromClass, List<Class> traceList) {
        // 添加到调用链
        traceList.add(toClass);
        if (toClass == fromClass) {
            return true;
        }
        // 递归遍历子节点
        List<Class> subToList = map.getOrDefault(toClass, new ArrayList<>());
        for (Class item : subToList) {
            if (ifTraceContains(item, fromClass, traceList)) {
                return true;
            }
        }
        // 不存在，则移除当前节点
        traceList.remove(toClass);
        return false;
    }

    private List<Class> putIfNotPresentAndGet(Class fromClass, List<Class> list) {
        List<Class> toList = map.get(fromClass);
        if (toList == null) {
            toList = list;
            map.put(fromClass, toList);
        }
        return toList;
    }

}
