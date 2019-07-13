package com.bage;

import com.bage.generator.ValueGenerator;
import com.bage.util.Logger;

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
        Object obj = valueGenerator.generateClassFieldValue(cls);
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
