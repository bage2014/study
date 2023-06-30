package com.bage.study.best.practice.service;

import com.bage.study.best.practice.model.User;

import java.util.List;

public interface UserMockService {
    User mockOne();

    List<User> mockBatch(int n);
}
