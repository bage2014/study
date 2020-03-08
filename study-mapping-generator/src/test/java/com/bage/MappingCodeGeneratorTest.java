package com.bage;

import com.bage.domain.ClassAttribute;
import com.bage.domain.ComplexDomain;
import com.bage.domain.FieldAttribute;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class MappingCodeGeneratorTest {

    @Test
    public void getInstance(){
        Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new MyExclusionStrategy()).create();

        ClassStructureGenerator mappingCodeGenerator = new ClassStructureGenerator();
        ClassAttribute classAttribute = mappingCodeGenerator.getClassAttribute(ComplexDomain.class);
//        System.out.println(gson.toJson(classAttribute));

        CodeGenerator codeGenerator = new CodeGenerator();

//        List<List<FieldAttribute>> classArray = mappingCodeGenerator.toList(classAttribute);
//        prints(classArray);

        Map<String,List<FieldAttribute>> map = mappingCodeGenerator.toMap(classAttribute);
        prints(map);


//        System.out.println(codeGenerator.getCode(classAttribute));
    }

    private void prints(Map<String, List<FieldAttribute>> map) {
        Set<Map.Entry<String, List<FieldAttribute>>> entries = map.entrySet();
        map.forEach((key,value) -> {
            System.out.print(key);
            System.out.print("-");
            System.out.print(Arrays.toString(value.stream().map(item -> item.getName()).collect(Collectors.toList()).toArray()));
            System.out.println();
        });

    }

    private void prints(List<List<FieldAttribute>> classArray) {
        for (List<FieldAttribute> classAttributes : classArray) {
            for (FieldAttribute classAttribute : classAttributes) {
                System.out.print(classAttribute.getClassOf() + ":" + classAttribute.getName() + "\t");
            }
            System.out.println();
        }
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
