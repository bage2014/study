package com.bage;

import com.bage.domain.User;
import com.bage.generator.DefaultValueGeneratorImpl;
import com.bage.generator.ValueGenerator;
import org.junit.Test;

public class InstanceBuilderTest {

    @Test
    public void getInstance(){

        InstanceBuilder builder = new InstanceBuilder();
        builder.setCls(User.class);
        ValueGenerator valueGenerator = new DefaultValueGeneratorImpl();

        valueGenerator.getDefaultData().setIntValue(1002);
        valueGenerator.getDefaultData().setStringValue("dsds");

        builder.setValueGenerator(valueGenerator);
        System.out.println(builder.build());
    }

}
