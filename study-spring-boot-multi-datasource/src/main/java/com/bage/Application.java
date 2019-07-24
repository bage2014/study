package com.bage;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application implements CommandLineRunner {

	@Autowired
	@Qualifier("jdbcTemplateOne")
	JdbcTemplate jdbcTemplateOne;
	@Autowired
	@Qualifier("jdbcTemplateTwo")
	JdbcTemplate jdbcTemplateTwo;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public void run(String... args) throws Exception {
		System.out.println(jdbcTemplateOne);
		System.out.println(jdbcTemplateTwo);

	}

	@org.springframework.context.annotation.Configuration
	@MapperScan(basePackages = "com.example.mapper.ds1", sqlSessionFactoryRef = "sqlSessionFactory")
	static class Ds1Configuration {
		private final MyBatisConfigurationSupport support;

		public Ds1Configuration(MyBatisConfigurationSupport support) {
			this.support = support;
		}

		@Bean
		public DataSource dataSource() {
			return support.createDataSource("ds1");
		}

		@Bean
		public SqlSessionFactoryBean sqlSessionFactory() {
			return support.createSqlSessionFactoryBean(dataSource());
		}

	}

	@org.springframework.context.annotation.Configuration
	@MapperScan(basePackages = "com.example.mapper.ds2", sqlSessionFactoryRef = "sqlSessionFactory2")
	static class Ds2Configuration {
		private final MyBatisConfigurationSupport support;

		public Ds2Configuration(MyBatisConfigurationSupport support) {
			this.support = support;
		}

		@Bean
		public DataSource dataSource2() {
			return support.createDataSource("ds2");
		}

		@Bean
		public SqlSessionFactoryBean sqlSessionFactory2() {
			return support.createSqlSessionFactoryBean(dataSource2());
		}

	}

	@Component
	static class MyBatisConfigurationSupport {
		private final DataSourceProperties dataSourceProperties;
		private final MybatisProperties myBatisProperties;
		private final ResourceLoader resourceLoader;

		public MyBatisConfigurationSupport(DataSourceProperties dataSourceProperties, MybatisProperties myBatisProperties,
										   ResourceLoader resourceLoader) {
			this.dataSourceProperties = dataSourceProperties;
			this.myBatisProperties = myBatisProperties;
			this.resourceLoader = resourceLoader;
		}

		public DataSource createDataSource(String instanceName) {
			DataSource dataSource = DataSourceBuilder.create()
					.driverClassName(dataSourceProperties.determineDriverClassName())
					.url(dataSourceProperties.determineUrl().replaceFirst("testdb", instanceName))
					.username(dataSourceProperties.determineUsername()).password(dataSourceProperties.determinePassword())
					.build();
			ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
					resourceLoader.getResource("classpath:schema.sql"));
			populator.execute(dataSource);
			return dataSource;
		}

		public SqlSessionFactoryBean createSqlSessionFactoryBean(DataSource dataSource) {
			SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
			factoryBean.setVfs(SpringBootVFS.class);
			factoryBean.setDataSource(dataSource);
			Configuration configuration = new Configuration();
			if (myBatisProperties.getConfiguration() != null) {
				BeanUtils.copyProperties(myBatisProperties.getConfiguration(), configuration);
			}
			// omit ...
			factoryBean.setConfiguration(configuration);
			return factoryBean;
		}

	}
}