package com.bage.study.best.practice.mapping;

import com.bage.study.best.practice.model.SexEnum;
import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.repo.UserEntity;
import com.bage.study.best.practice.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public Collection<UserEntity> mapping(List<User> userList) {
        if (userList == null) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::mapping).collect(Collectors.toList());
    }
}
