package com.bage.study.jasypt;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EncryptablePropertySource("encrypted.properties")
public class AppConfigForJasyptSimple {

    @Value("${encrypted.property}")
    private String property;

    public String getProperty() {
        return property;
    }

    public String getPasswordUsingEnvironment(Environment environment) {
        return environment.getProperty("encrypted.property");
    }

}
