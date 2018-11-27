package com.bage.study.docker.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		Connection connection = null;
		try {
			String url = "jdbc:mysql://192.168.146.133:3306/mydb?useUnicode=true&characterEncoding=utf-8&useSSL=true";
			String user = "bage";
			String password = "bage";
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("是否成功连接MySQL数据库" + connection);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if(connection != null){
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
}
