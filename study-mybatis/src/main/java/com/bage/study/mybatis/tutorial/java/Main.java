package com.bage.study.mybatis.tutorial.java;

import com.bage.study.mybatis.Weather;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import com.bage.study.mybatis.WeatherDataSourceFactory;
import com.bage.study.mybatis.WeatherMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

public class Main {

	public static void main(String[] args) {
		System.out.println("java配置方式执行结果。。。。。。");
		WeatherDataSourceFactory weatherDataSourceFactory = new WeatherDataSourceFactory();
		Properties properties = new Properties();
		properties.setProperty("driver", "com.mysql.jdbc.Driver");
		properties.setProperty("url", "jdbc:mysql://localhost:3306/mydb");
		properties.setProperty("username", "root");
		properties.setProperty("password", "root");
		weatherDataSourceFactory.setProperties(properties);
		DataSource dataSource = weatherDataSourceFactory.getDataSource();
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment("development", transactionFactory, dataSource);
		Configuration configuration = new Configuration(environment);
		configuration.addMapper(WeatherMapper.class);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		SqlSession session = sqlSessionFactory.openSession();
		try {
			WeatherMapper mapper = session.getMapper(WeatherMapper.class);
			List<Weather> weathers = mapper.selectAll();
			for (int i = 0; i < weathers.size(); i++) {
				System.out.println(weathers.get(i));
			}
		} finally {
			session.close();
		}
	}

}
