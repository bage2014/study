package com.bage.generator;

import com.bage.config.DefaultConfig;
import com.bage.util.Logger;

import java.io.File;
import java.lang.reflect.Field;
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
                field.set(object, generateListValue());
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

    protected abstract List generateListValue();

    protected abstract Map generateMapValue();

    protected abstract Set generateSetValue();

    protected abstract File generateFileValue();

    protected abstract Object generateObjectValue();
    ///////////////////////////////     常用类型赋值     ///////////////////////////////

}
