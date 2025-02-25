package com.bage.study.shadow;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class AppConfig {

    @Bean
    @Primary
    public DataSource datasource() throws SQLException {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // 配置第 1 个数据源
        HikariDataSource dataSource1 = new HikariDataSource();
        dataSource1.setDriverClassName("org.h2.Driver");
        dataSource1.setJdbcUrl("jdbc:h2:mem:split_db_1?serverTimezone=UTC&useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true");
        dataSource1.setUsername("root");
        dataSource1.setPassword("root");
        dataSourceMap.put("split_db_1", dataSource1);

        // 配置第二个数据源
        HikariDataSource dataSource2 = new HikariDataSource();
        dataSource2.setDriverClassName("org.h2.Driver");
        dataSource2.setJdbcUrl("jdbc:h2:mem:split_db_2?serverTimezone=UTC&useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true");
        dataSource2.setUsername("root");
        dataSource2.setPassword("root");
        dataSourceMap.put("split_db_2", dataSource2);

        // 配置Order表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration("tb_order", "split_db_${1..2}.tb_order_${2019..2020}");
        TableRuleConfiguration orderItemTableRuleConfig = new TableRuleConfiguration("tb_order_item", "split_db_${1..2}.tb_order_item_${2019..2020}");
        TableRuleConfiguration addressTableRuleConfig = new TableRuleConfiguration("tb_address", "split_db_${1..2}.tb_address_${2019..2020}");

        // 配置分库 + 分表策略
//        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "ds${user_id % 2}"));
//        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "t_order${user_id % 2}"));

        // 配置分库 + 分表策略
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("order_id", new PreciseModuloShardingDatabaseAlgorithm()));
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("create_time", new PreciseModuloShardingTableAlgorithm()));

        // 配置分片规则
        Collection<TableRuleConfiguration> tableRuleConfigs = shardingRuleConfig.getTableRuleConfigs();
        tableRuleConfigs.add(orderTableRuleConfig);
        tableRuleConfigs.add(orderItemTableRuleConfig);
        tableRuleConfigs.add(addressTableRuleConfig);

        // 省略配置order_item表规则...
        // ...

        // 获取数据源对象
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new Properties());

    }
}
