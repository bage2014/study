package com.familytree.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DatabaseInitializer {

    private final DataSource dataSource;
    private final String activeProfile;

    public DatabaseInitializer(DataSource dataSource, @Value("${spring.profiles.active}") String activeProfile) {
        this.dataSource = dataSource;
        this.activeProfile = activeProfile;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void initializeDatabase() {
        // 只在生产环境执行初始化
        if ("prod".equals(activeProfile)) {
            try (Connection connection = dataSource.getConnection()) {
                // 检查数据库是否已初始化
                if (!isDatabaseInitialized(connection)) {
                    System.out.println("开始初始化数据库...");
                    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
                    populator.addScript(new ClassPathResource("db/init.sql"));
                    populator.execute(dataSource);
                    System.out.println("数据库初始化完成");
                } else {
                    System.out.println("数据库已初始化，跳过初始化步骤");
                }
            } catch (SQLException e) {
                System.err.println("数据库初始化失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private boolean isDatabaseInitialized(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'users'"
            );
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }
}