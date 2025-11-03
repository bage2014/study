package com.bage;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EnhancedObjectMerger {
    
    // 缓存类的字段信息，提高性能
    private static final Map<Class<?>, List<Field>> FIELD_CACHE = new ConcurrentHashMap<>();
    
    /**
     * 合并两个对象的属性
     * @param target 目标对象
     * @param source 源对象
     * @param ignoreNull 是否忽略null值
     * @param <T> 对象类型
     * @return 合并后的对象
     */
    public static <T> T mergeObjects(T target, T source, boolean ignoreNull) {
        return mergeObjects(target, source, ignoreNull, new String[0]);
    }
    
    /**
     * 合并两个对象的属性
     * @param target 目标对象
     * @param source 源对象
     * @param ignoreNull 是否忽略null值
     * @param excludeFields 排除的字段名
     * @param <T> 对象类型
     * @return 合并后的对象
     */
    public static <T> T mergeObjects(T target, T source, boolean ignoreNull, String... excludeFields) {
        if (target == null || source == null) {
            return target;
        }
        
        if (!target.getClass().equals(source.getClass())) {
            throw new IllegalArgumentException("目标对象和源对象类型必须相同");
        }
        
        Set<String> excludedSet = new HashSet<>(Arrays.asList(excludeFields));
        List<Field> fields = getFields(target.getClass());
        
        for (Field field : fields) {
            if (excludedSet.contains(field.getName())) {
                continue;
            }
            
            try {
                field.setAccessible(true);
                Object sourceValue = field.get(source);
                
                // 根据ignoreNull参数决定是否覆盖
                if (!ignoreNull || sourceValue != null) {
                    field.set(target, sourceValue);
                }
            } catch (IllegalAccessException e) {
                System.err.println("无法访问字段: " + field.getName());
            }
        }
        
        return target;
    }
    
    /**
     * 批量合并对象列表
     * @param objects 对象列表
     * @param <T> 对象类型
     * @return 合并后的对象
     */
    @SafeVarargs
    public static <T> T mergeMultipleObjects(T... objects) {
        if (objects == null || objects.length == 0) {
            return null;
        }
        
        T result = objects[0];
        for (int i = 1; i < objects.length; i++) {
            result = mergeObjects(result, objects[i], true);
        }
        return result;
    }
    
    /**
     * 获取类的所有字段（包括父类）
     */
    private static List<Field> getFields(Class<?> clazz) {
        return FIELD_CACHE.computeIfAbsent(clazz, k -> {
            List<Field> fields = new ArrayList<>();
            Class<?> currentClass = clazz;
            while (currentClass != null && currentClass != Object.class) {
                fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
                currentClass = currentClass.getSuperclass();
            }
            return fields;
        });
    }
}