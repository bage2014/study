package com.bage.study.best.practice.mapping;

import com.bage.study.best.practice.model.SexEnum;
import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.repo.UserEntity;
import com.bage.study.best.practice.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserMapping {

    public User mapping(UserEntity param) {
        User result = new User();
        result.setId(param.getId());
        result.setFirstName(param.getFirstName());
        result.setLastName(param.getLastName());
        result.setSex(SexEnum.valueOfCode(param.getSex()));
        result.setBirthday(DateUtils.date2LocalDate(param.getBirthday()));
        result.setAddress(param.getAddress());
        result.setEmail(param.getEmail());
        result.setPhone(param.getPhone());
        result.setRemark(param.getRemark());
        return result;
    }


    public UserEntity mapping(User param) {
        UserEntity result = new UserEntity();
        result.setId(param.getId());
        result.setFirstName(param.getFirstName());
        result.setLastName(param.getLastName());
        result.setSex(param.getSex().getCode());
        result.setBirthday(DateUtils.localDate2Date(param.getBirthday()));
        result.setAddress(param.getAddress());
        result.setEmail(param.getEmail());
        result.setPhone(param.getPhone());
        result.setRemark(param.getRemark());
        return result;
    }
}
