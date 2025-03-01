//package com.bage.study.shadow;
//
//import javax.sql.DataSource;
//
//public final class ShadowConfiguration {
//
//    @Override
//    public DataSource getDataSource() throws SQLException {
//        Map<String, DataSource> dataSourceMap = createDataSourceMap();
//        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, createRuleConfigurations(), createShardingSphereProps());
//    }
//
//    private Map<String, DataSource> createDataSourceMap() {
//        Map<String, DataSource> result = new LinkedHashMap<>();
//        result.put("ds", DataSourceUtil.createDataSource("demo_ds"));
//        result.put("ds_shadow", DataSourceUtil.createDataSource("shadow_demo_ds"));
//        return result;
//    }
//
//    private Collection<RuleConfiguration> createRuleConfigurations() {
//        Collection<RuleConfiguration> result = new LinkedList<>();
//        ShadowRuleConfiguration shadowRule = new ShadowRuleConfiguration();
//        shadowRule.setDataSources(createShadowDataSources());
//        shadowRule.setTables(createShadowTables());
//        shadowRule.setShadowAlgorithms(createShadowAlgorithmConfigurations());
//        result.add(shadowRule);
//        return result;
//    }
//
//    private Map<String, ShadowDataSourceConfiguration> createShadowDataSources() {
//        Map<String, ShadowDataSourceConfiguration> result = new LinkedHashMap<>();
//        result.put("shadow-data-source", new ShadowDataSourceConfiguration("ds", "ds_shadow"));
//        return result;
//    }
//
//    private Map<String, ShadowTableConfiguration> createShadowTables() {
//        Map<String, ShadowTableConfiguration> result = new LinkedHashMap<>();
//        result.put("t_user", new ShadowTableConfiguration(Collections.singletonList("shadow-data-source"), createShadowAlgorithmNames()));
//        return result;
//    }
//
//    private Collection<String> createShadowAlgorithmNames() {
//        Collection<String> result = new LinkedList<>();
//        result.add("user-id-insert-match-algorithm");
//        result.add("simple-hint-algorithm");
//        return result;
//    }
//
//    private Map<String, AlgorithmConfiguration> createShadowAlgorithmConfigurations() {
//        Map<String, AlgorithmConfiguration> result = new LinkedHashMap<>();
//        Properties userIdInsertProps = new Properties();
//        userIdInsertProps.setProperty("operation", "insert");
//        userIdInsertProps.setProperty("column", "user_type");
//        userIdInsertProps.setProperty("value", "1");
//        result.put("user-id-insert-match-algorithm", new AlgorithmConfiguration("VALUE_MATCH", userIdInsertProps));
//        return result;
//    }
//}
