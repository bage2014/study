package com.bage;

import com.bage.domain.ClassAttribute;
import com.bage.domain.FieldAttribute;
import com.bage.parser.ClassParser;
import com.bage.parser.FieldParser;
import com.bage.parser.GenericParser;
import com.bage.util.JsonUtils;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

/**
 *
 */
public class ClassStructureGenerator {


    public ClassAttribute getClassAttribute(Class cls) {
        ClassAttribute classAttribute = new ClassAttribute();

        // 获取当前类信息
        classAttribute.setName(cls.getSimpleName());
        classAttribute.setClassOf(cls.getName());

        // 获取当前类的字段信息
        List<FieldAttribute> fieldAttributes = getFieldAttribute(cls);

        // 获取父类的字段信息
        Class[] classes = ClassParser.getAllSuperClass(cls);
        for (Class clsItem : classes) {
            List<FieldAttribute> superFieldAttributes = getFieldAttribute(clsItem);
            fieldAttributes.addAll(superFieldAttributes);
        }

        // 递归所有子属性
        recursiveFieldAttribute(fieldAttributes);

        classAttribute.setFields(fieldAttributes);
        return classAttribute;
    }

    private void recursiveFieldAttribute(List<FieldAttribute> fieldAttributes) {
        if (Objects.isNull(fieldAttributes) || fieldAttributes.isEmpty()) {
            return;
        }

        for (FieldAttribute fieldAttribute : fieldAttributes) {
            ClassAttribute classAttribute = recursiveFieldAttribute(fieldAttribute);
            fieldAttribute.setClassAttribute(classAttribute);
        }
    }

    private ClassAttribute recursiveFieldAttribute(FieldAttribute fieldAttribute) {
        if (Objects.isNull(fieldAttribute)) {
            return null;
        }
        try {
            Class cls = fieldAttribute.getCls();
            if (!ClassParser.isRecusiveClass(cls)) {
                return null;
            }

            // 泛型
            if (cls == List.class || cls.isAssignableFrom(List.class)) {
                Type type = GenericParser.getGenericTypeClassName(fieldAttribute.getField(), 0);
                return getGenericTypeClassAttribute(fieldAttribute, type);
            }
            if (cls == Map.class || cls.isAssignableFrom(Map.class)) {
                Type type = GenericParser.getGenericTypeClassName(fieldAttribute.getField(), 1);
                return getGenericTypeClassAttribute(fieldAttribute, type);
            }
            if (cls == Set.class || cls.isAssignableFrom(Set.class)) {
                Type type = GenericParser.getGenericTypeClassName(fieldAttribute.getField(), 0);
                return getGenericTypeClassAttribute(fieldAttribute, type);
            }

            if (cls.getName().startsWith("java")) {
                return null;
            }
            // 当成自定义对象,需要递归获取操作
            return getCustomClassAttribute(fieldAttribute, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ClassAttribute getCustomClassAttribute(FieldAttribute fieldAttribute, Class cls) {
        ClassAttribute classAttribute = new ClassAttribute();
        classAttribute.setName(cls.getSimpleName());
        classAttribute.setClassOf(cls.getName());
        List<FieldAttribute> fieldAttribute1 = getFieldAttribute(cls);
        classAttribute.setFields(fieldAttribute1);
        ClassAttribute classAttribute1 = getClassAttribute(cls);
        fieldAttribute.setClassAttribute(classAttribute1);
        return classAttribute;
    }

    private ClassAttribute getGenericTypeClassAttribute(FieldAttribute fieldAttribute, Type type) throws ClassNotFoundException {
        Class clsType = Class.forName(type.getTypeName());
        if (!ClassParser.isRecusiveClass(clsType)) {
            return null;
        }
        ClassAttribute classAttribute = new ClassAttribute();
        classAttribute.setName(clsType.getSimpleName());
        classAttribute.setClassOf(clsType.getName());
        classAttribute.setFields(getFieldAttribute(clsType));
        fieldAttribute.setClassAttribute(getClassAttribute(clsType));
        return classAttribute;
    }

    private List<FieldAttribute> getFieldAttribute(Class cls) {
        // 获取当前类的字段信息
        List<FieldAttribute> fieldAttributes = new ArrayList<>();
        Field[] fields = FieldParser.getDeclaredFields(cls);
        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            FieldAttribute item = new FieldAttribute();
            item.setName(field.getName());
            item.setClassOf(field.getType().getName());
            item.setCls(field.getType());
            item.setField(field);
            fieldAttributes.add(item);
        }
        return fieldAttributes;
    }


    public List<List<FieldAttribute>> toList(ClassAttribute classAttribute) {
        List<List<FieldAttribute>> listlist = new ArrayList<>();
        // root ，第一层子节点
        List<FieldAttribute> fields = classAttribute.getFields();
        if (Objects.nonNull(fields)) {
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
                if (Objects.nonNull(attribute)) {
                    List<FieldAttribute> fieldAttributeList = attribute.getFields();
                    if (Objects.nonNull(fieldAttributeList)) {
                        List<FieldAttribute> newList = new ArrayList<>();
                        for (FieldAttribute item : fieldAttributeList) {
                            newList.add(item);
                        }
                        if (newList.size() > 0) {
                            listlist.add(newList);
                        }
                    }
                }
            }
        }
        return listlist;
    }

    public Map<String, List<FieldAttribute>> toMap(ClassAttribute param) {
        // clone
        ClassAttribute classAttribute = JsonUtils.fromJson(JsonUtils.toJson(param), ClassAttribute.class);

        Map<String, List<FieldAttribute>> map = new HashMap<>();
        // 第一层
        List<FieldAttribute> subList = classAttribute.getFields();
        map.put(KeyUtils.getMapKey(classAttribute), cloneList(subList));

        // 便利子节点
        while (subList.size() > 0) {
            FieldAttribute attribute = subList.remove(0);
            ClassAttribute subClassAttribute = attribute.getClassAttribute();
            if (Objects.nonNull(subClassAttribute) && !map.containsKey(subClassAttribute.getClassOf())) {
                List<FieldAttribute> fields = subClassAttribute.getFields();
                if (Objects.nonNull(fields) && fields.size() > 0) {
                    subList.addAll(fields);
                    map.put(KeyUtils.getMapKey(classAttribute), cloneList(fields));
                }
            }
        }
        return map;
    }

    private List<FieldAttribute> cloneList(List<FieldAttribute> subList) {
        String json = JsonUtils.toJson(subList);
        return JsonUtils.fromJson(json, new TypeToken<List<FieldAttribute>>() {
        }.getType());
    }
}
