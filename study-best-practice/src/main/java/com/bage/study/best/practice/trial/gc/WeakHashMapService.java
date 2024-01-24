package com.bage.study.best.practice.trial.gc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * GC 时候，WeakHashMap 内容 自动会被回收 
 */
@Component
@Slf4j
public class WeakHashMapService {

    private static Map<String,String> map = new WeakHashMap<String,String>();

    public int size(){
        return map.size();
    }

    public String get(String key){
        return map.get(key);
    }

    public String put(String key,String value){
        return map.put(key,value);
    }

    public String remove(String key){
        return map.remove(key);
    }

}
