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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MappingCodeGeneratorTest {

    @Test
    public void getInstance(){
        Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new MyExclusionStrategy()).create();

        ClassStructureGenerator mappingCodeGenerator = new ClassStructureGenerator();
        ClassAttribute classAttribute = mappingCodeGenerator.getClassAttribute(ComplexDomain.class);
//        System.out.println(gson.toJson(classAttribute));

        CodeGenerator codeGenerator = new CodeGenerator();

        List<List<FieldAttribute>> classArray = toArray(classAttribute);
        prints(classArray);


//        System.out.println(codeGenerator.getCode(classAttribute));
    }

    private void prints(List<List<FieldAttribute>> classArray) {
        for (List<FieldAttribute> classAttributes : classArray) {
            for (FieldAttribute classAttribute : classAttributes) {
                System.out.print(classAttribute.getClassOf() + ":" + classAttribute.getName() + "\t");
            }
            System.out.println();
        }
    }

    private List<List<FieldAttribute>> toArray(ClassAttribute classAttribute) {
        List<List<FieldAttribute>> listlist = new ArrayList<>();
        // root ，第一层子节点
        List<FieldAttribute> fields = classAttribute.getFields();
        if(Objects.nonNull(fields)){
            List<FieldAttribute> list = new ArrayList<>();
            for (int i = 0; i < fields.size(); i++) { // 初始，1 个，只有根节点
                list.add(fields.get(i));
            }
            listlist.add(list);
        }

        // 开始从第二层开始便利
        for (int i = 0; i < listlist.size(); i++) { // 初始，1 个，只有根节点
            List<FieldAttribute> fieldAttributes = listlist.get(i);
            for (int j = 0; j < fieldAttributes.size(); j++) {
                FieldAttribute fieldAttribute = fieldAttributes.get(j);
                ClassAttribute attribute = fieldAttribute.getClassAttribute();
                if(Objects.nonNull(attribute)){
                    List<FieldAttribute> fieldAttributeList = attribute.getFields();
                    if(Objects.nonNull(fieldAttributeList)){
                        List<FieldAttribute> newList = new ArrayList<>();
                        for (FieldAttribute item : fieldAttributeList) {
                            newList.add(item);
                        }
                        if(newList.size() > 0){
                            listlist.add(newList);
                        }
                    }
                }
            }
        }
        return listlist;
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
