package com.bage.study.best.practice.trial.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 * https://www.runoob.com/w3cnote/jdbc-use-guide.html
 * <p>
 * //1.加载驱动程序
 * Class.forName("com.mysql.jdbc.Driver");
 * //2. 获得数据库连接
 * Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
 * //3.操作数据库，实现增删改查
 * Statement stmt = conn.createStatement();
 * ResultSet rs = stmt.executeQuery("SELECT user_name, age FROM imooc_goddess");
 * //如果有数据，rs.next()返回true
 * while(rs.next()){
 * System.out.println(rs.getString("user_name")+" 年龄："+rs.getInt("age"));
 * }
 */
public class ConnectionUtils {

    public static final String URL = "jdbc:mysql://localhost:3306/mydbpro?zeroDateTimeBehavior=convertToNull&characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
    public static final String USER = "bage";
    public static final String PWD = "bage";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws Exception {
        Connection conn = DriverManager.getConnection(URL, USER, PWD);
        System.out.println("getConnection:::" + conn);
        return conn;
    }

}
