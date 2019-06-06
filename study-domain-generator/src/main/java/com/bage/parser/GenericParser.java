package com.bage.parser;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型解析
 */
public class GenericParser {

    /**
     * 泛型解析
     * @param field
     * @param index
     * @return
     */
    public static Type getGenericTypeClassName(Field field, int index) {
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        return getGenericTypeClassName(genericType,index);
    }

    /**
     * 泛型解析
     * @param type
     * @param index
     * @return
     */
    public static Type getGenericTypeClassName(ParameterizedType type, int index) {
        Type[] listActualTypeArguments = type.getActualTypeArguments();
        return listActualTypeArguments[index];
    }


}
