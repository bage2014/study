package org.mybatis.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

public class Main {

	public static void main(String[] args) {
		//System.out.println("xml配置方式执行结果。。。。。。");
		//xml();
		System.out.println("java配置方式执行结果。。。。。。");
		java();
	}

	private static void java() {
		WeatherDataSourceFactory weatherDataSourceFactory = new WeatherDataSourceFactory();
		Properties properties = new Properties();
		properties.setProperty("driver", "com.mysql.jdbc.Driver");
		properties.setProperty("url", "jdbc:mysql://localhost:3306/my_db");
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

	private static void xml() {
		String resource = "org/mybatis/example/mybatis-config.xml";
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			SqlSession session = sqlSessionFactory.openSession();
			try {
				List<Weather> weathers = session.selectList("org.mybatis.example.BlogMapper.selectBlog");
				for (int i = 0; i < weathers.size(); i++) {
					System.out.println(weathers.get(i));
				}
			} finally {
				session.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
