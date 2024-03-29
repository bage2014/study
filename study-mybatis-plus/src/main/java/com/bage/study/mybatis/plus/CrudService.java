package com.bage.study.mybatis.plus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 参考链接 https://github.com/baomidou/mybatis-plus-samples/blob/master/mybatis-plus-sample-crud/src/test/java/com/baomidou/mybatisplus/samples/crud/CrudTest.java
 */
@Component
public class CrudService {

    @Autowired
    private UsersServiceImpl myService;

    public int insert(User user){
        return myService.save(user) ? 1 : -1;
    }
    public int insertBatch(List<User> list){
        return myService.saveBatch(list) ? 1 : -1;
    }


    public int delete(Long id){
        return myService.removeById(id) ? 1 : -1;
    }


    public int update(User user){
        return myService.updateById(user) ? 1 : -1;
    }

    public User query(Long id){
        return myService.getById(id);
    }
    public List<User> query(String name){
        LambdaQueryWrapper<User> select = Wrappers.<User>lambdaQuery().eq(User::getName,name);
        return myService.list(select);
    }

    public Page page(int current, int size,String name) {
        LambdaQueryWrapper<User> select = Wrappers.<User>lambdaQuery().eq(User::getName,name);
        return myService.page(new Page<>(current,size), select);
    }
}
