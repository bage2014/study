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
    public static Type getListTypeClassName(Field field, int index) {
        ParameterizedType listGenericType = (ParameterizedType) field.getGenericType();
        return getListTypeClassName(listGenericType,index);
    }

    public static Type getListTypeClassName(ParameterizedType listGenericType,int index) {
        Type[] listActualTypeArguments = listGenericType.getActualTypeArguments();
        return listActualTypeArguments[index];
    }


}
