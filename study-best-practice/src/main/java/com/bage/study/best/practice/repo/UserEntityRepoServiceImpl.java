package com.bage.study.best.practice.repo;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserEntityRepoServiceImpl extends ServiceImpl<UserEntityMapper, UserEntity> implements UserEntityRepoService {
   
}