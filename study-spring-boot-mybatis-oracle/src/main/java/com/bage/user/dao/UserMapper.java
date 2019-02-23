package com.bage.user.dao;

import java.util.List;
import java.util.Map;

import com.bage.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper {

    List<User> queryAll();
    
    @Select("select * from users where id=#{id}")
    User queryOne(@Param("id") String id);
    

    Map<String,Object> querySys(@Param("id") String id);

}
