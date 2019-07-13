package com.bage;

import com.bage.domain.ComplexDomain;
import com.bage.domain.User;
import com.bage.generator.DefaultValueGeneratorImpl;
import com.bage.generator.ValueGenerator;
import com.google.gson.GsonBuilder;
import org.junit.Test;

public class InstanceBuilderTest {

    @Test
    public void getInstance(){

        InstanceBuilder builder = new InstanceBuilder();
        builder.setCls(ComplexDomain.class);

        ValueGenerator valueGenerator = new DefaultValueGeneratorImpl();

        valueGenerator.getDefaultData().setIntValue(100);
        valueGenerator.getDefaultData().setStringValue("myStrPrefix-");

        builder.setValueGenerator(valueGenerator);
        Object build = builder.build();
        System.out.println(new GsonBuilder().serializeNulls().create().toJson(build));
    }

    @Test
    public void getUserInstance(){

        InstanceBuilder builder = new InstanceBuilder();
        builder.setCls(User.class);

        ValueGenerator valueGenerator = new DefaultValueGeneratorImpl();

        builder.setValueGenerator(valueGenerator);
        System.out.println(builder.build());
    }

}
