package com.bage.generator;

import com.bage.config.DefaultConfig;
import com.bage.parser.GenericParser;
import com.bage.util.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

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
                field.set(object, generateIntValue());
            } else if (cls == Double.class) {
                field.set(object, generateDoubleValue());
            } else if (cls == Float.class) {
                field.set(object, generateFloatValue());
            } else if (cls == Long.class) {
                field.set(object, generateLongValue());
            } else if (cls == Short.class) {
                field.set(object, generateShortValue());
            } else if (cls == Boolean.class) {
                field.set(object, generateBooleanValue());
            } else if (cls == Byte.class) {
                field.set(object, generateByteValue());
            } else if (cls == Character.class) {
                field.set(object, generateCharValue());
            }
            // Java 常用包装类型
            else if (cls == String.class) {
                field.set(object, generateStringValue());
            } else if (cls == Date.class) {
                field.set(object, generateDateValue());
            }

            // 泛型
            else if (cls == List.class) {
                List value = generateListValue();
                Type type = GenericParser.getGenericTypeClassName(field, 0);
                setGenericValue(type,value);
                field.set(object, value);
            } else if (cls == Map.class) {
                Map value = generateMapValue();
                Type type = GenericParser.getGenericTypeClassName(field, 1);
                setGenericValue(type,value);
                field.set(object, value);
            } else if (cls == Set.class) {
                Set value = generateSetValue();
                Type type = GenericParser.getGenericTypeClassName(field, 0);
                setGenericValue(type,value);
                field.set(object, value);
            }

            // 枚举
            else if(cls.isEnum()){
                field.set(object, generateEnumValue(cls));
            }
            else {
                Logger.debug("不支持的实例化类型 cls={}", cls);
            }

            Logger.debug("字段赋值：field={}, value={} ", field, field.get(object));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setGenericValue(Type type,Object parent) throws ClassNotFoundException {

        Object value = null;
        if(type instanceof ParameterizedType ){
            ParameterizedType subType = (ParameterizedType) type;

            Type rawType = subType.getRawType();
            if(Class.forName(rawType.getTypeName()) == List.class){ // List
                Type typeClassName = GenericParser.getGenericTypeClassName(subType, 0);
                value = generateListValue();
                setGenericValue(typeClassName,value);

            }else if(Class.forName(rawType.getTypeName()) == Map.class){ // Map
                Type typeClassName = GenericParser.getGenericTypeClassName(subType, 1);
                value = generateMapValue();
                setGenericValue(typeClassName,value);
            }else if(Class.forName(rawType.getTypeName()) == Set.class){ // Set
                Type typeClassName = GenericParser.getGenericTypeClassName(subType, 0);
                value = generateSetValue();
                setGenericValue(typeClassName,value);
            }
        } else {
            value = generateFieldValue(Class.forName(type.getTypeName()));
        }

        // 值回填
        if(parent instanceof List){
            List listParent = ((List) parent);
            listParent.add(value);
        } else if(parent instanceof Map){
            Map mapParent = ((Map) parent);
            mapParent.put(String.valueOf(new Random().nextInt(1000)),value);
        } else if(parent instanceof Set){
            Set setParent = ((Set) parent);
            setParent.add(value);
        }

    }

    public Object generateFieldValue(Class cls) {
        try {
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
            }
            // 泛型
            else if (cls == List.class) {
                return generateListValue();
            } else if (cls == Map.class) {
                return generateMapValue();
            } else if (cls == Set.class) {
                return generateSetValue();
            }
            // 枚举
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
    ///////////////////////////////     常用类型赋值     ///////////////////////////////

    ///////////////////////////////     泛型类型赋值     ///////////////////////////////
    protected abstract List generateListValue();

    protected abstract Map generateMapValue();

    protected abstract Set generateSetValue();

    ///////////////////////////////     常用类型赋值     ///////////////////////////////

    protected abstract Object generateObjectValue();


}
