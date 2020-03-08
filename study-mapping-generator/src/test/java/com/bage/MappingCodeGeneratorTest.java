package com.bage;

import com.bage.domain.ClassAttribute;
import com.bage.domain.ComplexDomain;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.lang.reflect.Field;

public class MappingCodeGeneratorTest {

    @Test
    public void getInstance(){
        Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new MyExclusionStrategy()).create();

        MappingCodeGenerator mappingCodeGenerator = new MappingCodeGenerator();
        ClassAttribute classAttribute = mappingCodeGenerator.getClassAttribute(ComplexDomain.class);
        System.out.println(gson.toJson(classAttribute));
    }
    static class MyExclusionStrategy implements ExclusionStrategy {

        public MyExclusionStrategy() {
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            if (f.getDeclaredClass() == Class.class) {
                return true; //过滤掉name字段
            }
            if (f.getDeclaredClass() == Field.class) {
                return true; //过滤掉name字段
            }
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
