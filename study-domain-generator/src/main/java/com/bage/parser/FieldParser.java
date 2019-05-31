package com.bage.parser;

import java.lang.reflect.Field;

public class FieldParser {

    /**
     *
     * @param cls
     * @return
     */
    public static Field[] getDeclaredFields(Class cls) {
        return cls.getDeclaredFields();
    }

}
