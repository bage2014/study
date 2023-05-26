package com.bage.study.common.repo.mysql.mybatis.plus.pro.mock;

import com.bage.study.common.repo.mysql.mybatis.plus.pro.model.SexEnum;
import com.bage.study.common.repo.mysql.mybatis.plus.pro.model.UserPro;
import com.bage.study.common.repo.mysql.mybatis.plus.pro.service.UserProDataService;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserProDataServiceMockImpl implements UserProDataService {
    Faker faker = new Faker();

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
        String name = faker.name().fullName(); // Miss Samanta Schmidt
        String firstName = ; // Emory
        String lastName = faker.name().lastName(); // Barton

        String streetAddress = ; // 60018 Sawayn Brooks Suite 449

        UserPro userPro = new UserPro();
        userPro.setAddress(faker.address().streetAddress());
        userPro.setFirstName(faker.name().firstName());
        userPro.setLastName(faker.name().lastName());
        userPro.setSex(i % 3 == 0 ? SexEnum.Female : SexEnum.Male);
        userPro.setBirthday(LocalDate.now().plusDays(0- i%30000));
        userPro.setEmail(faker.date().birthday());
        private Long id;
        private String firstName;
        private String lastName;
        private SexEnum sex;
        private LocalDate birthday;
        private String address;
        private String email;
        private String phone;
        private String remark;
        return userPro;
    }
}
