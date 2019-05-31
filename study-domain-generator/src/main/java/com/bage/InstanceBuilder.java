package com.bage;

import com.bage.generator.ValueGenerator;
import com.bage.parser.FieldParser;
import com.bage.util.Logger;

import java.lang.reflect.Field;

public class InstanceBuilder {

    private Class cls;
    private ValueGenerator valueGenerator;

    public InstanceBuilder(){
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    public ValueGenerator getValueGenerator() {
        return valueGenerator;
    }

    public void setValueGenerator(ValueGenerator valueGenerator) {
        this.valueGenerator = valueGenerator;
    }

    public Object build(){

        checkBeforeBuild();

        Logger.debug("开始构建：Class=%s , ValueGenerator=%s",cls,valueGenerator);

        Object obj = null;
        try {
            obj = cls.newInstance();

            // 类自身字段和父类字段

            // 获取当前类的字段信息
            Field[] fields = FieldParser.getDeclaredFields(cls);
            Logger.debug("当前类字段信息：fields=%s ", fields);
            for(Field field : fields){
                valueGenerator.generateFieldValue(field,obj);
            }

            // 获取父类的

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private void checkBeforeBuild() {
        if(cls == null){
            throw new IllegalArgumentException("Class cls can not be null");
        }
        if(valueGenerator == null){
            throw new IllegalArgumentException("ValueGenerator valueGenerator can not be null");
        }
    }
}
