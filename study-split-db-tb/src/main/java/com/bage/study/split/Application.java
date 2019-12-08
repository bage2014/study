package com.bage.study.split;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ComplexShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Application {

    public static void main(String[] args) throws SQLException {
        OrderRepository jdbcOrderRepository = init1();
//        jdbcOrderRepository.createTableIfNotExists();
//
        List<Order> orders = jdbcOrderRepository.queryByPage(null,1,50);
        System.out.println(orders.size());

        insert(jdbcOrderRepository);

    }

    private static void insert(OrderRepository jdbcOrderRepository) {
        System.out.println("---------------------------- Insert Data ----------------------------");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 1; i <= 10; i++) {
            Order order = newOrder(i);
            order.setOrderId(i);
            order.setUserId(i);
            order.setStatus("INSERT_TEST");
            order.setChannel("android");
            LocalDateTime ldt = LocalDateTime.parse("20" + (i > 5 ? 20 : 19) + "-02-10 16:30", dtf);
            order.setCreateTime(ldt);
            order.setSupplier("kkday");
            int insert = jdbcOrderRepository.insert(order);
            //System.out.println(insert);
        }
    }

    private static Order newOrder(int i) {
        Order order = new Order();
        order.setOrderId(i);
        order.setStatus("sta" + i);
        order.setUserId(i);
        return order;
    }

    private static OrderRepository init1() throws SQLException {
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
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
//        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("order_id", new PreciseModuloShardingDatabaseAlgorithm()));
//        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("create_time", new PreciseModuloShardingTableAlgorithm()));

        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new ComplexShardingStrategyConfiguration(
                StringUtils.collectionToDelimitedString(Arrays.asList("order_id","create_time"), ","),
                new MyDBComplexKeysShardingAlgorithm()));
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration(
                StringUtils.collectionToDelimitedString(Arrays.asList("order_id","create_time"), ","),
                new MyTBComplexKeysShardingAlgorithm()));

        // 配置分片规则
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);

        // 省略配置order_item表规则...
        // ...

        // 获取数据源对象
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new Properties());

        return new OrderRepositoryImpl(dataSource);
    }

    private static OrderRepository init2() throws SQLException {
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
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new Properties());

        return new OrderRepositoryImpl(dataSource);
    }

}
