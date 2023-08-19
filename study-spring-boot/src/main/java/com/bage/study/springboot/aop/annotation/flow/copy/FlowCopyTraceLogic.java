package com.bage.study.springboot.aop.annotation.flow.copy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FlowCopyTraceLogic {

    private Map<String, List<String>> map = new ConcurrentHashMap<>();
    private ThreadLocal<Map<String, List<String>>> mapThreadLocal = new ThreadLocal();
    private static final Logger log = LoggerFactory.getLogger(FlowCopyTraceLogic.class);

    /**
     * @param from
     * @param to
     * @return
     */
    public boolean check(String from, String to) {
        // 表示 from 到 fromList 每个节点 都有直接 流量复制关系
        Map<String, List<String>> currentMap = getMap();
        List<String> fromList = putIfNotPresentAndGet(currentMap,from, new ArrayList<>());
        putIfNotPresentAndGet(currentMap,to, new ArrayList<>());
        // 流量复制链
        List<String> traceList = new ArrayList<>();
        // 判断是否有直接 或者 间接关系
        boolean isContains = ifTraceContains(to, from, traceList);
        if (isContains) {
            String collect = traceList.stream().collect(Collectors.joining("->"));
            log.info("can not copy to an exist flow, cause exist call: {}", collect);
            return false;
        }

        // 添加到直接关系
        fromList.add(to);
//        mapThreadLocal.set(currentMap);
        return true;
    }

    private boolean ifTraceContains(String to, String from, List<String> traceList) {
        // 添加到调用链
        traceList.add(to);
        if (Objects.equals(to, from)) {
            return true;
        }
        // 递归遍历子节点
        List<String> subToList = getMap().getOrDefault(to, new ArrayList<>());
        for (String item : subToList) {
            if (ifTraceContains(item, from, traceList)) {
                return true;
            }
        }
        // 不存在，则移除当前节点
        traceList.remove(to);
        return false;
    }

    private List<String> putIfNotPresentAndGet(Map<String, List<String>> currentMap,String from, List<String> list) {
        List<String> toList = currentMap.get(from);
        if (toList == null) {
            toList = list;
            currentMap.put(from, toList);
        }
        return toList;
    }

    private Map<String, List<String>> getMap() {
        return map;
//        Map<String, List<String>> stringListMap = mapThreadLocal.get();
//        if(stringListMap == null){
//            stringListMap = new ConcurrentHashMap<>();
//            mapThreadLocal.set(stringListMap);
//        }
//        return stringListMap;
    }

}
