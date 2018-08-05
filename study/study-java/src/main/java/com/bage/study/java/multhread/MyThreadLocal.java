package com.bage.study.java.multhread;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * ThreadLocal 使用类
 * ThreadLocal 是线程的局部变量， 是每一个线程所单独持有的，其他线程不能对其进行访问<br>
 * 最常见的ThreadLocal使用场景为 用来解决 数据库连接、Session管理等。<br>
 * @author bage
 *
 */
public class MyThreadLocal {

	public static void main(String[] args) {
		
		// 存在多线程下，Connection出现报错问题
		Connection conn=DBUtil.getConnection();
		service(conn);
		
	}
	
	private static void service(Connection conn) {
		 try {
	            //获取链接
	            conn.setAutoCommit(false);//关闭自动提交事务(开启事务);
	            //执行操作,更新产品，插入日志
	            // TODO
	            //最后提交事务
	            conn.commit();
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally {
	            //关闭连接
	            DBUtil.closeConnection();
	        }
		
	}
	
}

class DBUtil{

	// 避免多线程下，线程1还在操作，但是线程2进行了commit后出现报错情况
	// 使用 ThreadLocal来存每个线程下的自己的 Connection
	static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>();

	
	/**
	 * 存在多线程下，线程1还在操作，但是线程2进行了commit后出现报错
	 * @return
	 */
	public static Connection getConnection() {
		return connectionHolder.get();
	}

	public static void closeConnection() {
		try {
			connectionHolder.get().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}