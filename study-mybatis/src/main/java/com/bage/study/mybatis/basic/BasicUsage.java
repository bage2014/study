package com.bage.study.mybatis.basic;

import com.bage.study.mybatis.common.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class BasicUsage {

    public static void main(String[] args) throws Exception {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            String statement = "com.bage.study.mybatis.common.dao.UserMapper.queryOne";
            User user = session.selectOne(statement, 1);
            System.out.println(user);
        }

    }

}
