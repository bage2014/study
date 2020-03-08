package com.bage;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Bean(name = "dynamicSource")
    public DynamicDataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 默认数据源
        // 默认数据源
        DataSource dataSource1 = DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/db1?useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .password("uname")
                .username("upassword")
                .build();
        DataSource dataSource2 = DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/db2?useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .password("uname")
                .username("upassword")
                .build();

        dynamicDataSource.setDefaultTargetDataSource(dataSource1);

        // 配置多数据源
        Map<Object, Object> dataSources = new HashMap<Object, Object>();
        dataSources.put("db1", dataSource1);
        dataSources.put("db2", dataSource2);
        dynamicDataSource.setTargetDataSources(dataSources);
        return dynamicDataSource;
    }

}
