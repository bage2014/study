package com.bage;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DbConfig {


    @org.springframework.context.annotation.Configuration
    @MapperScan(basePackages = "com.bage.mapper.db1", sqlSessionFactoryRef = "sqlSessionFactory1")
    static class Ds1Configuration {

        @Bean
        @Primary
        @ConfigurationProperties("app.datasource.first")
        public DataSourceProperties firstDataSourceProperties() {
            return new DataSourceProperties();
        }

        @Bean
        @Primary
        @ConfigurationProperties("app.datasource.first.configuration")
        public HikariDataSource dataSource1() {
            return firstDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
        }

        @Bean
        @Primary
        public SqlSessionFactory sqlSessionFactory1(@Qualifier("dataSource1") DataSource datasource) throws Exception {

            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("/mybatis/config1.xml"));
            sqlSessionFactoryBean.setDataSource(datasource);
            return sqlSessionFactoryBean.getObject();
        }
        @Bean
        @Autowired
        @Primary
        DataSourceTransactionManager transactionManager1(@Qualifier("dataSource1") DataSource datasource) {
            DataSourceTransactionManager txm  = new DataSourceTransactionManager(datasource);
            return txm;
        }
    }

    @org.springframework.context.annotation.Configuration
    @MapperScan(basePackages = "com.bage.mapper.db2", sqlSessionFactoryRef = "sqlSessionFactory2")
    static class Ds2Configuration {

        @Bean
        @ConfigurationProperties("app.datasource.second")
        public DataSourceProperties secondDataSourceProperties() {
            return new DataSourceProperties();
        }

        @Bean
        @ConfigurationProperties("app.datasource.second.configuration")
        public HikariDataSource dataSource2() {
            return secondDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
        }

        @Bean
        public SqlSessionFactory sqlSessionFactory2(@Qualifier("dataSource2") DataSource datasource) throws Exception {

            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("/mybatis/config2.xml"));
            sqlSessionFactoryBean.setDataSource(datasource);
            return sqlSessionFactoryBean.getObject();
        }
        @Bean
        @Autowired
        DataSourceTransactionManager transactionManager2(@Qualifier("dataSource2") DataSource datasource) {
            DataSourceTransactionManager txm  = new DataSourceTransactionManager(datasource);
            return txm;
        }
    }

    //////////////////////////////////////// jdbc 方式 /////////////////////////////////////////////////
    @Bean
    public JdbcTemplate jdbcTemplate1(@Qualifier("dataSource1") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    @Bean
    public JdbcTemplate jdbcTemplate2(@Qualifier("dataSource2") DataSource ds) {
        return new JdbcTemplate(ds);
    }


}
