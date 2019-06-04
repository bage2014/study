package com.bage;

import com.bage.parser.GenericParser;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class GenericParserTest {

    private List<List<String>> list;

    @Test
    public void test() throws NoSuchFieldException {
        Field field = GenericParserTest.class.getDeclaredField("list");
        Type listTypeClassName = GenericParser.getListTypeClassName(field, 0);


        System.out.println(listTypeClassName);
        System.out.println(listTypeClassName instanceof ParameterizedType);
        ParameterizedType type = (ParameterizedType) listTypeClassName;
        System.out.println(type.getActualTypeArguments()[0]);

        System.out.println(listTypeClassName.getTypeName());
        System.out.println(type.getOwnerType());
        System.out.println(type.getRawType());
        System.out.println(listTypeClassName.getClass());

    }

}
