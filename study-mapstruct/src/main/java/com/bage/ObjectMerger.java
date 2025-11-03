package com.bage;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class ObjectMerger {
    
    /**
     * 合并两个对象的属性
     * @param target 目标对象（将被覆盖的对象）
     * @param source 源对象（覆盖目标的对象）
     * @param <T> 对象类型
     * @return 合并后的对象
     */
    public static <T> T mergeObjects(T target, T source) {
        if (target == null || source == null) {
            return target;
        }
        
        Class<?> clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();
        
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object sourceValue = field.get(source);
                
                // 如果源对象的属性不为null，则覆盖目标对象的属性
                if (sourceValue != null) {
                    field.set(target, sourceValue);
                }
            } catch (IllegalAccessException e) {
                System.err.println("无法访问字段: " + field.getName());
            }
        }
        
        return target;
    }
}