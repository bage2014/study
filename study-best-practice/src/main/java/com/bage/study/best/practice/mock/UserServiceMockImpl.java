package com.bage.study.best.practice.mock;

import cn.binarywang.tools.generator.ChineseMobileNumberGenerator;
import cn.binarywang.tools.generator.EmailAddressGenerator;
import com.bage.study.best.practice.model.SexEnum;
import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.service.UserService;
import com.github.javafaker.Faker;
import com.github.jsonzou.jmockdata.JMockData;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserServiceMockImpl {

    public List<User> getInitData(int n) {
        int max = n;
        List<User> list = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            list.add(mockUser(i));
        }
        return list;
    }

    private User mockUser(int i) {
        Faker faker = new Faker();
        User user = new User();
        user.setAddress(faker.address().streetAddress());
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setSex(i % 3 == 0 ? SexEnum.Female : SexEnum.Male);
        user.setBirthday(JMockData.mock(LocalDate.class));
        user.setEmail(EmailAddressGenerator.getInstance().generate());
        user.setPhone(ChineseMobileNumberGenerator.getInstance()
                .generate());
        user.setRemark("remark:" + i);
        return user;
    }

}
