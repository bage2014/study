package com.bage.util;


import com.bage.parser.ClassParser;
import com.bage.parser.FieldParser;

import java.lang.reflect.Field;

public class MappingUtils {

    public static String getMappingMethod(String methodName, Class cls) {
        StringBuilder sb = new StringBuilder();
        Class[] allClass = ClassParser.getAllClass(cls);
        for (int i = 0; i < allClass.length; i++) {
            Class clsItem = allClass[i];
            Field[] declaredFields = FieldParser.getDeclaredFields(clsItem);
            for (int j = 0; j < declaredFields.length; j++) {
                Field declaredField = declaredFields[j];
                String name = declaredField.getName();
                name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                sb.append("a.set")
                        .append(name)
                        .append("(")
                        .append("b.").append("get").append(name).append("()")
                        .append(");\n")
                ;
            }
        }
        return sb.toString();
    }

}
