package com.bage;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.bage.ds1", sqlSessionFactoryRef = AppConfig.ds1)
@MapperScan(basePackages = "com.bage.ds2", sqlSessionFactoryRef = AppConfig.ds2)
public class AppConfig {

    public static final String ds1 = "sqlSessionFactory1";
    public static final String ds2 = "sqlSessionFactory2";

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.first")
    public DataSourceProperties firstDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.first.configuration")
    public HikariDataSource firstDataSource() {
        return firstDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }
    @Bean(ds1)
    @Primary
    public SqlSessionFactory sqlSessionFactory1(@Qualifier("firstDataSource") DataSource datasource) throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("/mybatis/mybatis-conf.xml"));
        sqlSessionFactoryBean.setDataSource(datasource);
        return sqlSessionFactoryBean.getObject();
    }


    @Bean
    @ConfigurationProperties("app.datasource.second")
    public DataSourceProperties secondDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("app.datasource.second.configuration")
    public HikariDataSource secondDataSource() {
        return secondDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }
    @Bean(ds2)
    @Primary
    public SqlSessionFactory sqlSessionFactory2(@Qualifier("secondDataSource") DataSource datasource) throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("/mybatis/mybatis-conf.xml"));
        sqlSessionFactoryBean.setDataSource(datasource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name="tm1")
    @Autowired
    @Primary
    DataSourceTransactionManager tm1(@Qualifier("firstDataSource") DataSource datasource) {
        DataSourceTransactionManager txm  = new DataSourceTransactionManager(datasource);
        return txm;
    }

    @Bean(name="tm2")
    @Autowired
    DataSourceTransactionManager tm2(@Qualifier ("secondDataSource") DataSource datasource) {
        DataSourceTransactionManager txm  = new DataSourceTransactionManager(datasource);
        return txm;
    }



    //////////////////////////////////////// jdbc 方式 /////////////////////////////////////////////////
    @Bean
    public JdbcTemplate jdbcTemplateOne(@Qualifier("firstDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean
    public JdbcTemplate jdbcTemplateTwo(@Qualifier("secondDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }


}
