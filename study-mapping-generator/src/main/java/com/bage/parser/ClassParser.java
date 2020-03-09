package com.bage.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ClassParser {

    /**
     * 当前类所有的超类
     *
     * @param cls
     * @return
     */
    public static Class[] getAllSuperClass(Class cls) {
        List<Class> list = new ArrayList<>();
        Class superCls = cls.getSuperclass();
        while (superCls != Object.class && Objects.nonNull(superCls)) {
            list.add(superCls);
            superCls = superCls.getSuperclass();
        }

        return list.toArray(new Class[list.size()]);
    }

    public static boolean isRecusiveClass(String cls) {
        // 基本类型
        if ("int".equals(cls)) {
            return false;
        }
        if ("double".equals(cls)) {
            return false;
        }
        if ("float".equals(cls)) {
            return false;
        }
        if ("long".equals(cls)) {
            return false;
        }
        if ("short".equals(cls)) {
            return false;
        }
        if ("boolean".equals(cls)) {
            return false;
        }
        if ("byte".equals(cls)) {
            return false;
        }
        if ("char".equals(cls)) {
            return false;
        }
        try {
            return isRecusiveClass(Class.forName(cls));
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isRecusiveClass(Class cls) {
        // 基本类型
        if (int.class == cls) {
            return false;
        }
        if (double.class == cls) {
            return false;
        }
        if (float.class == cls) {
            return false;
        }
        if (long.class == cls) {
            return false;
        }
        if (short.class == cls) {
            return false;
        }
        if (boolean.class == cls) {
            return false;
        }
        if (byte.class == cls) {
            return false;
        }
        if (char.class == cls) {
            return false;
        }
        // 基本类型包装类
        if (cls == Integer.class) {
            return false;
        }
        if (cls == Double.class) {
            return false;
        }
        if (cls == Float.class) {
            return false;
        }
        if (cls == Long.class) {
            return false;
        }
        if (cls == Short.class) {
            return false;
        }
        if (cls == Boolean.class) {
            return false;
        }
        if (cls == Byte.class) {
            return false;
        }
        if (cls == Character.class) {
            return false;
        }
        // Java 常用包装类型
        if (cls == String.class) {
            return false;
        }
        if (cls == Date.class) {
            return false;
        }
        // 枚举
        if (cls.isEnum()) {
            return false;
        }
        return true;
    }
}
