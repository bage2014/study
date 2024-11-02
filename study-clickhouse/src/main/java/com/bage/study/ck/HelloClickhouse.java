package com.bage.study.ck;

import java.sql.*;
import java.util.Properties;

public class HelloClickhouse {

    public static void main(String[] args) throws SQLException {

        Connection connection = getConnection();

        createTable(connection);

        writeData(connection);

        readData(connection);

    }

    private static void readData(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("select * from jdbc_test");
        while (rs.next()) {
            System.out.println(String.format("idx: %s str: %s", rs.getString(1), rs.getString(2)));
        }

    }

    private static void writeData(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "insert into jdbc_test select col1, col2 from input('col1 Int8, col2 String')")) {
            for (int i = 0; i < 10; i++) {
                ps.setInt(1, i);
                ps.setString(2, "test:" + i); // col1
                // parameters will be write into buffered stream immediately in binary format
                ps.addBatch();
            }
            // stream everything on-hand into ClickHouse
            ps.executeBatch();
        }
    }

    private static void createTable(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS jdbc_test(idx Int8, str String) ENGINE = MergeTree ORDER BY idx");
    }

    private static Connection getConnection() throws SQLException {
        Properties prop = new Properties();
        prop.setProperty("username", "default");
        prop.setProperty("password", "<password>");
        return DriverManager.getConnection("jdbc:ch:https://<host>:8443?insert_quorum=auto", prop);
    }
}
