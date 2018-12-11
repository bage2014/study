package com.bage.study.mybatis.tutorial.xml;

import com.bage.study.mybatis.Weather;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XmlMain {

	public static void main(String[] args) {
		System.out.println("xml配置方式执行结果。。。。。。");
		String resource = "mybatis-config.xml";
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			//  属性加载顺序验证
			properties(sqlSessionFactory);

			SqlSession session = sqlSessionFactory.openSession(true);
			try {
				String statement = "com.bage.study.mybatis.WeatherMapper.selectBlog";
				List<Weather> weathers = session.selectList(statement);
				// 在查询一次，验证缓存
				weathers = session.selectList(statement);

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

	private static void properties(SqlSessionFactory sqlSessionFactory) {

		Configuration con = sqlSessionFactory.getConfiguration();
		String multiPropertyValue = con.getVariables().getProperty("multiProperty");
		System.out.println("multiPropertyValue:" + multiPropertyValue);

	}

}
