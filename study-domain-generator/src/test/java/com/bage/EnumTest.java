package com.bage;

import com.bage.domain.Sex;

public class EnumTest {

    public static void main3(String[] args) throws IllegalAccessException, InstantiationException {

        Object obj = null;

        Class<Sex> cls = Sex.class;

        String name = cls.getDeclaredFields()[0].getName();

        if (cls.isEnum()) {
            obj = Enum.valueOf(cls, name);
            System.out.println(cls);

        }
        //obj = cls.newInstance();
        System.out.println(obj);


    }

}
