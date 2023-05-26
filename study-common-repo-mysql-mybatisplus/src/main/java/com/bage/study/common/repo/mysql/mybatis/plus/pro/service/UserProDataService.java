package com.bage.study.common.repo.mysql.mybatis.plus.pro.service;

import com.bage.study.common.repo.mysql.mybatis.plus.pro.model.UserPro;

import java.util.List;

public interface UserProDataService {

    List<UserPro> getInitData();
}
