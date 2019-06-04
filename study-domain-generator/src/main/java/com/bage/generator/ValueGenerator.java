package com.bage.generator;

import com.bage.config.DefaultConfig;
import com.bage.parser.GenericParser;
import com.bage.util.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ValueGenerator {

    private DefaultConfig defaultData = DefaultConfig.newInstance();

    public void setDefaultData(DefaultConfig defaultData) {
        this.defaultData = defaultData;
    }

    public DefaultConfig getDefaultData() {
        return defaultData;
    }

    public void generateFieldValue(Field field, Object object) {
        try {
            Class cls = field.getType();

            field.setAccessible(true); // 强行赋值

            // 基本类型
            if (int.class == cls) {
                field.setInt(object, generateIntValue());
            } else if (double.class == cls) {
                field.setDouble(object, generateDoubleValue());
            } else if (float.class == cls) {
                field.setFloat(object, generateFloatValue());
            } else if (long.class == cls) {
                field.setLong(object, generateLongValue());
            } else if (short.class == cls) {
                field.setShort(object, generateShortValue());
            } else if (boolean.class == cls) {
                field.setBoolean(object, generateBooleanValue());
            } else if (byte.class == cls) {
                field.setByte(object, generateByteValue());
            } else if (char.class == cls) {
                field.setChar(object, generateCharValue());
            }
            // 基本类型包装类
            else if (cls == Integer.class) {
                field.setInt(object, generateIntValue());
            } else if (cls == Double.class) {
                field.setDouble(object, generateDoubleValue());
            } else if (cls == Float.class) {
                field.setFloat(object, generateFloatValue());
            } else if (cls == Long.class) {
                field.setLong(object, generateLongValue());
            } else if (cls == Short.class) {
                field.setShort(object, generateShortValue());
            } else if (cls == Boolean.class) {
                field.setBoolean(object, generateBooleanValue());
            } else if (cls == Byte.class) {
                field.setByte(object, generateByteValue());
            } else if (cls == Character.class) {
                field.setChar(object, generateCharValue());
            }
            // Java 常用包装类型
            else if (cls == String.class) {
                field.set(object, generateStringValue());
            } else if (cls == Date.class) {
                field.set(object, generateDateValue());
            } else if (cls == List.class) {
                Type type = GenericParser.getListTypeClassName(field, 0);
                List listValue = generateListValue(type);
                field.set(object, listValue);
            } else if (cls == Map.class) {
                field.set(object, generateMapValue());
            } else if (cls == Set.class) {
                field.set(object, generateSetValue());
            } else if (cls == File.class) {
                field.set(object, generateFileValue());
            } else if(cls.isEnum()){
                field.set(object, generateEnumValue(cls));
            }
            else {
                // TODO
            }

            Logger.debug("字段赋值：field=%s, value=%s ", field, field.get(object));

        } catch (Exception e) {

        }
    }


    public Object generateFieldValue(Type type) {
        try {

            Class cls = type.getClass();
            if(type instanceof ParameterizedType){
                ParameterizedType parameterizedType = ((ParameterizedType) type);
                parameterizedType.getRawType().getClass();
            }

            // 基本类型
            if (int.class == cls) {
                return generateIntValue();
            } else if (double.class == cls) {
                return generateDoubleValue();
            } else if (float.class == cls) {
                return generateFloatValue();
            } else if (long.class == cls) {
                return generateLongValue();
            } else if (short.class == cls) {
                return generateShortValue();
            } else if (boolean.class == cls) {
                return generateBooleanValue();
            } else if (byte.class == cls) {
                return generateByteValue();
            } else if (char.class == cls) {
                return generateCharValue();
            }
            // 基本类型包装类
            else if (cls == Integer.class) {
                return generateIntValue();
            } else if (cls == Double.class) {
                return generateDoubleValue();
            } else if (cls == Float.class) {
                return generateFloatValue();
            } else if (cls == Long.class) {
                return generateLongValue();
            } else if (cls == Short.class) {
                return generateShortValue();
            } else if (cls == Boolean.class) {
                return generateBooleanValue();
            } else if (cls == Byte.class) {
                return generateByteValue();
            } else if (cls == Character.class) {
                return generateCharValue();
            }
            // Java 常用包装类型
            else if (cls == String.class) {
                return generateStringValue();
            } else if (cls == Date.class) {
                return generateDateValue();
            } else if (cls == List.class) {
                System.out.println("");
            } else if (cls == Map.class) {
                return generateMapValue();
            } else if (cls == Set.class) {
                return generateSetValue();
            } else if (cls == File.class) {
                return generateFileValue();
            } else if(cls.isEnum()){
                return generateEnumValue(cls);
            }
            // 泛型
            else if(cls.isEnum()){

                return generateEnumValue(cls);
            }

            else {
                // TODO
            }
        } catch (Exception e) {

        }
        return null;
    }

    public Object generateFieldValsssue(Type type) {
        try {
            String typeName = type.getTypeName();
            // 基本类型
            if (Integer.class.toString().equals(typeName)) {
                return generateIntValue();
            } else if (Integer.class.toString().equals(typeName)) {
                return generateDoubleValue();
            } else if (Float.class.toString().equals(typeName)) {
                return generateFloatValue();
            } else if (Long.class.toString().equals(typeName)) {
                return generateLongValue();
            } else if (Short.class.toString().equals(typeName)) {
                return generateShortValue();
            } else if (Boolean.class.toString().equals(typeName)) {
                return generateBooleanValue();
            } else if (Byte.class.toString().equals(typeName)) {
                return generateByteValue();
            } else if (Character.class.toString().equals(typeName)) {
                return generateCharValue();
            }
            // Java 常用包装类型
            else if (String.class.toString().equals(typeName)) {
                return generateStringValue();
            } else if (Date.class.toString().equals(typeName)) {
                return generateDateValue();
            } else if (List.class.toString().equals(typeName)) {
                Type subType = GenericParser.getListTypeClassName(((ParameterizedType) type), 0);
                return generateListValue(subType);
            } else if (Map.class.toString().equals(typeName)){
                return generateMapValue();
            } else if (Set.class.toString().equals(typeName)) {
                return generateSetValue();
            } else if (File.class.toString().equals(typeName)) {
                return generateFileValue();
            } else if(Class.forName(typeName).isEnum()){
                return generateEnumValue(Class.forName(typeName));
            }
        } catch (Exception e) {

        }
        return null;
    }

    protected abstract Object generateEnumValue(Class cls);

    ///////////////////////////////     基本类型赋值     ///////////////////////////////
    protected abstract Character generateCharValue();

    protected abstract Byte generateByteValue();

    protected abstract Boolean generateBooleanValue();

    protected abstract Short generateShortValue();

    protected abstract Long generateLongValue();

    protected abstract Float generateFloatValue();

    protected abstract Double generateDoubleValue();

    protected abstract Integer generateIntValue();
    ///////////////////////////////     基本类型赋值     ///////////////////////////////

    ///////////////////////////////     常用类型赋值     ///////////////////////////////
    protected abstract String generateStringValue();

    protected abstract Date generateDateValue();

    protected abstract List generateListValue(Type type);

    protected abstract Map generateMapValue();

    protected abstract Set generateSetValue();

    protected abstract File generateFileValue();

    protected abstract Object generateObjectValue();
    ///////////////////////////////     常用类型赋值     ///////////////////////////////

}
