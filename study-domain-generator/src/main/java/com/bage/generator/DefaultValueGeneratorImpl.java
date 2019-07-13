package com.bage.generator;

import com.bage.parser.FieldParser;
import com.bage.util.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 默认值生成
 */
public class DefaultValueGeneratorImpl extends ValueGenerator {

    @Override
    public Object generateClassFieldValue(Class cls) {
        Object obj = null;
        try {
            obj = cls.newInstance();

            // 类自身字段和父类字段

            // 获取当前类的字段信息
            Field[] fields = FieldParser.getDeclaredFields(cls);
            Logger.debug("当前类字段信息：fields=%s ", fields);
            for(Field field : fields){
                generateBasicFieldValue(field,obj);
            }
            // 获取父类的
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    protected Object generateEnumValue(Class cls) {
        return getDefaultData().getEnumValue(cls);
    }

    public Character generateCharValue() {
        return getDefaultData().getCharValue();
    }

    public Byte generateByteValue() {
        return getDefaultData().getByteValue();
    }

    public Boolean generateBooleanValue() {
        return getDefaultData().isBooleanValue();
    }

    public Short generateShortValue() {
        return getDefaultData().getShortValue();
    }

    public Long generateLongValue() {
        return getDefaultData().getLongValue();
    }

    public Float generateFloatValue() {
        return getDefaultData().getFloatValue();
    }

    public Double generateDoubleValue() {
        return getDefaultData().getDoubleValue();
    }

    public Integer generateIntValue() {
        return getDefaultData().getIntValue();
    }

    public String generateStringValue() {
        return getDefaultData().getStringValue();
    }

    public Date generateDateValue() {
        return getDefaultData().getDateValue();
    }

    public Object generateObjectValue() {
        return getDefaultData().getObjectValue();
    }

    public List generateListValue() {
        return getDefaultData().getListValue();
    }

    public Set generateSetValue() {
        return getDefaultData().getSetValue();
    }

    public File generateFileValue() {
        return getDefaultData().getFileValue();
    }

    public Map generateMapValue() {
        return getDefaultData().getMapValue();
    }

}
