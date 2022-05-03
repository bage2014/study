package com.bage.study.jmeter;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.bage.auth.server.*.mapper", sqlSessionFactoryRef = AppConstants.APP_PREF + "SqlSessionFactory")
public class DataSourceConfig {


    @Bean(AppConstants.APP_PREF + "DataSource")
    public DataSource dataSource() {
        DataSourceBuilder<HikariDataSource> builder = DataSourceBuilder.create().type(HikariDataSource.class);
        HikariDataSource hikariDataSource = builder
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://127.0.0.1:3306/mydb?useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true")
                .username("bage")
                .password("bage")
                .build();
        hikariDataSource.setAutoCommit(true);//update自动提交设置
        hikariDataSource.setConnectionTestQuery("select 1");//连接查询语句设置
        hikariDataSource.setConnectionTimeout(5000);//连接超时时间设置
        hikariDataSource.setIdleTimeout(3000);//连接空闲生命周期设置
        hikariDataSource.setIsolateInternalQueries(false);//执行查询启动设置
        hikariDataSource.setMaximumPoolSize(500);//连接池允许的最大连接数量
        hikariDataSource.setMaxLifetime(1800000);//检查空余连接优化连接池设置时间,单位毫秒
        hikariDataSource.setMinimumIdle(10);//连接池保持最小空余连接数量
        hikariDataSource.setPoolName(AppConstants.APP_PREF + "HikariPool");//连接池名称
        return builder.build();
    }

    @Bean(AppConstants.APP_PREF + "SqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier(AppConstants.APP_PREF + "DataSource") DataSource datasource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/com/bage/auth/server/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);
        sqlSessionFactoryBean.setDataSource(datasource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(AppConstants.APP_PREF + "TransactionManager")
    DataSourceTransactionManager transactionManager(@Qualifier(AppConstants.APP_PREF + "DataSource") DataSource datasource) {
        return new DataSourceTransactionManager(datasource);
    }


}
