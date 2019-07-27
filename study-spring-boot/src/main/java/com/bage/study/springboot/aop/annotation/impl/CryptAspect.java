package com.bage.study.springboot.aop.annotation.impl;

import com.bage.study.springboot.aop.annotation.WithField;
import com.bage.study.springboot.aop.annotation.WithMethod;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Component
@Aspect
public class CryptAspect extends WithAspect {

    private static final Logger log = LoggerFactory.getLogger(CryptAspect.class);

    @Override
    protected void doAfterThrowing(Object target, Method method, Object[] args, WithMethod annotation, Object proxy, Object retVal, Throwable throwable) {
        super.doAfterThrowing(target, method, args, annotation, proxy, retVal, throwable);
        if (annotation.afterThrowing()) {
            // TODO
        }
    }

    @Override
    protected void doAfterReturning(Object target, Method method, Object[] args, WithMethod annotation, Object proxy, Object result) {
        super.doAfterReturning(target, method, args, annotation, proxy, result);
        if (annotation.afterReturning()) {
            if (Objects.isNull(result)) {
                return;
            }
            Class cls = result.getClass();
            CryptAction cryptAction = value -> URLCoderUtils.decode(value);
            if (List.class.isAssignableFrom(cls)) {
                Object listObj = result;
                if (Objects.nonNull(listObj)) {
                    List list = (List) listObj;
                    for (Object item : list) {
                        // 遍历 操作
                        updateFieldValue(item, cryptAction);
                    }
                }
            } else if (Map.class.isAssignableFrom(cls)) {
                Object mapObj = result;
                if (Objects.nonNull(mapObj)) {
                    Map map = (Map) mapObj;
                    map.forEach((key, value) -> {
                        // 遍历 操作
                        updateFieldValue(value, cryptAction);
                    });
                }
            } else if (Set.class.isAssignableFrom(cls)) {
                Object setObj = result;
                if (Objects.nonNull(setObj)) {
                    Set set = (Set) setObj;
                    set.forEach(value -> {
                        // 遍历 操作
                        updateFieldValue(value, cryptAction);
                    });
                }
            } else {
                // 对象
                updateFieldValue(result, cryptAction);
            }
        }
    }

    @Override
    protected void doBefore(Object target, Method method, Object[] args, WithMethod annotation, Object proxy) {
        super.doBefore(target, method, args, annotation, proxy);
        if (annotation.before()) {
            updateFieldValue(args, value -> URLCoderUtils.encode(value));
        }
    }

    private void updateFieldValue(Object[] args, CryptAction cryptAction) {
        if (Objects.nonNull(args)) {
            for (int i = 0; i < args.length; i++) {
                updateFieldValue(args[i], cryptAction);
            }
        }
    }

    private void updateFieldValue(Object obj, CryptAction cryptAction) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                Class cls = field.getType();
                updateFieldValue(cls, field, obj, cryptAction);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void updateFieldValue(Class cls, Field field, Object obj, CryptAction cryptAction) {
        try {
            // 基本类型
            if (int.class == cls) {
                // 基本类型不考虑
            } else if (double.class == cls) {
                // 基本类型不考虑
            } else if (float.class == cls) {
                // 基本类型不考虑
            } else if (long.class == cls) {
                // 基本类型不考虑
            } else if (short.class == cls) {
                // 基本类型不考虑
            } else if (boolean.class == cls) {
                // 基本类型不考虑
            } else if (byte.class == cls) {
                // 基本类型不考虑
            } else if (char.class == cls) {
                // 基本类型不考虑
            }
            // 基本类型包装类
            else if (cls == Integer.class) {
                // 非字符包装类不考虑
            } else if (cls == Double.class) {
                // 非字符包装类不考虑
            } else if (cls == Float.class) {
                // 非字符包装类不考虑
            } else if (cls == Long.class) {
                // 非字符包装类不考虑
            } else if (cls == Short.class) {
                // 非字符包装类不考虑
            } else if (cls == Boolean.class) {
                // 非字符包装类不考虑
            } else if (cls == Byte.class) {
                // 非字符包装类不考虑
            } else if (cls == Character.class) {
                // 非字符包装类不考虑
            }
            // 日期
            else if (cls == Date.class) {
                // 非字符包装类不考虑
            }
            // 枚举
            else if (cls.isEnum()) {
                // 非字符包装类不考虑
            }
            // Java 常用包装类型
            else if (String.class.isAssignableFrom(cls)) {
                WithField annotation = field.getAnnotation(WithField.class);
                if (annotation != null) {

                    field.setAccessible(true);

                    Object stringValue = field.get(obj);
                    field.set(obj, cryptAction.crypt((String) stringValue));

                    field.setAccessible(false);
                }
            }
            // 泛型
            else if (List.class.isAssignableFrom(cls)) {
                field.setAccessible(true);

                Object listObj = field.get(obj);
                if (Objects.nonNull(listObj)) {
                    List list = (List) listObj;
                    for (Object item : list) {
                        // 遍历 操作
                        updateFieldValue(item, cryptAction);
                    }
                }
                field.setAccessible(false);

            } else if (Map.class.isAssignableFrom(cls)) {
                field.setAccessible(true);

                Object mapObj = field.get(obj);
                if (Objects.nonNull(mapObj)) {
                    Map map = (Map) mapObj;
                    map.forEach((key, value) -> {
                        // 遍历 操作
                        updateFieldValue(value, cryptAction);
                    });
                }
                field.setAccessible(false);

            } else if (Set.class.isAssignableFrom(cls)) {
                field.setAccessible(true);

                Object setObj = field.get(obj);
                if (Objects.nonNull(setObj)) {
                    Set set = (Set) setObj;
                    set.forEach(value -> {
                        // 遍历 操作
                        updateFieldValue(value, cryptAction);
                    });
                }
                field.setAccessible(false);

            } else {
                field.setAccessible(true);

                Field[] fields = field.getType().getDeclaredFields();
                Object parentObj  = field.get(obj);
                for (Field subField : fields) {
                    updateFieldValue(subField.getType(), subField, parentObj, cryptAction);
                }

                field.setAccessible(false);

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

}
