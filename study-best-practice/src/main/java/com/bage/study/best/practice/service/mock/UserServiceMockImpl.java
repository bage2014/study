package com.bage.study.best.practice.service.mock;

import cn.binarywang.tools.generator.ChineseMobileNumberGenerator;
import cn.binarywang.tools.generator.EmailAddressGenerator;
import com.bage.study.best.practice.model.SexEnum;
import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.service.UserMockService;
import com.bage.study.best.practice.utils.DateUtils;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.github.jsonzou.jmockdata.JMockData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@Slf4j
public class UserServiceMockImpl implements UserMockService {

    @Override
    public User mockOne() {
        long start = System.currentTimeMillis();
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
        long end = System.currentTimeMillis();
        log.info("UserServiceMockImpl mockOne cost = {}", (end - start));
        return user;
    }

    @Override
    public List<User> mockBatch(int n) {
        long start = System.currentTimeMillis();

        List<User> list = new ArrayList<>(n);
        Faker faker = new Faker();

        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());

        for (int i = 0; i < n; i++) {
            User user = new User();
            user.setAddress(faker.address().streetAddress());
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setSex("Female".equalsIgnoreCase(faker.demographic().sex()) ? SexEnum.Female : SexEnum.Male);
            user.setBirthday(DateUtils.date2LocalDate(faker.date().birthday()));
            user.setEmail(fakeValuesService.bothify("????????##@gmail.com"));
            user.setPhone(faker.phoneNumber().cellPhone());
            user.setRemark(JMockData.mock(String.class));
            list.add(user);
        }
        long end = System.currentTimeMillis();
        log.info("UserServiceMockImpl mockOne cost = {}", (end - start));
        return list;
    }

}
