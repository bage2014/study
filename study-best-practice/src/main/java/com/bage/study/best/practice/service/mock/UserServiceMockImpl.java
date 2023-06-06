package com.bage.study.best.practice.service.mock;

import cn.binarywang.tools.generator.ChineseMobileNumberGenerator;
import cn.binarywang.tools.generator.EmailAddressGenerator;
import com.bage.study.best.practice.model.SexEnum;
import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.service.UserMockService;
import com.github.javafaker.Faker;
import com.github.jsonzou.jmockdata.JMockData;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserServiceMockImpl implements UserMockService {

    @Override
    public User mockOne() {
        Faker faker = new Faker();
        User user = new User();
        user.setAddress(faker.address().streetAddress());
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setSex(JMockData.mock(SexEnum.class));
        user.setBirthday(JMockData.mock(LocalDate.class));
        user.setEmail(EmailAddressGenerator.getInstance().generate());
        user.setPhone(ChineseMobileNumberGenerator.getInstance()
                .generate());
        user.setRemark(JMockData.mock(String.class));
        return user;
    }

}
