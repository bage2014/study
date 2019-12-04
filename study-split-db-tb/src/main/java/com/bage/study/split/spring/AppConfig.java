package com.bage.study.split.spring;

import com.bage.study.split.PreciseModuloShardingDatabaseAlgorithm;
import com.bage.study.split.PreciseModuloShardingTableAlgorithm;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class AppConfig {

    @Bean
    public DataSource datasource() throws SQLException {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // 配置第一个数据源
        BasicDataSource dataSource1 = new BasicDataSource();
        dataSource1.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource1.setUrl("jdbc:mysql://localhost:3306/split_db_1?serverTimezone=UTC&useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true");
        dataSource1.setUsername("root");
        dataSource1.setPassword("root");
        dataSourceMap.put("split_db_1", dataSource1);

        // 配置第二个数据源
        BasicDataSource dataSource2 = new BasicDataSource();
        dataSource2.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource2.setUrl("jdbc:mysql://localhost:3306/split_db_2?serverTimezone=UTC&useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true");
        dataSource2.setUsername("root");
        dataSource2.setPassword("root");
        dataSourceMap.put("split_db_2", dataSource2);

        // 配置Order表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration("tb_order", "split_db_${1..2}.tb_order_${2019..2020}");

        // 配置分库 + 分表策略
//        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "ds${user_id % 2}"));
//        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "t_order${user_id % 2}"));

        // 配置分库 + 分表策略
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("order_id", new PreciseModuloShardingDatabaseAlgorithm()));
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("create_time", new PreciseModuloShardingTableAlgorithm()));

        // 配置分片规则
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);

        // 省略配置order_item表规则...
        // ...

        // 获取数据源对象
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new Properties());

    }
}
