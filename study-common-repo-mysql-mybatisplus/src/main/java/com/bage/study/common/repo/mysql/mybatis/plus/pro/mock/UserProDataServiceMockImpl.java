package com.bage.study.common.repo.mysql.mybatis.plus.pro.mock;

import cn.binarywang.tools.generator.ChineseMobileNumberGenerator;
import cn.binarywang.tools.generator.EmailAddressGenerator;
import com.bage.study.common.repo.mysql.mybatis.plus.pro.model.SexEnum;
import com.bage.study.common.repo.mysql.mybatis.plus.pro.model.UserPro;
import com.bage.study.common.repo.mysql.mybatis.plus.pro.service.UserProDataService;
import com.github.javafaker.Faker;
import com.github.jsonzou.jmockdata.JMockData;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserProDataServiceMockImpl implements UserProDataService {
    @Override
    public List<UserPro> getInitData() {
        int max = 1000 * 10000;
        List<UserPro> list = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            list.add(mockUser(i));
        }
        return list;
    }

    private UserPro mockUser(int i) {
        Faker faker = new Faker();
        UserPro userPro = new UserPro();
        userPro.setAddress(faker.address().streetAddress());
        userPro.setFirstName(faker.name().firstName());
        userPro.setLastName(faker.name().lastName());
        userPro.setSex(i % 3 == 0 ? SexEnum.Female : SexEnum.Male);
        userPro.setBirthday(JMockData.mock(LocalDate.class));
        userPro.setEmail(EmailAddressGenerator.getInstance().generate());
        userPro.setPhone(ChineseMobileNumberGenerator.getInstance()
                .generate());
        userPro.setRemark("remark:" + i);
        return userPro;
    }

}
