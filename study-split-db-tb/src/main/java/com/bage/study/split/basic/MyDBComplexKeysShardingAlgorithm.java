package com.bage.study.split.basic;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;

public class MyDBComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm {
    @Override
    public Collection<String> doSharding(Collection availableTargetNames, ComplexKeysShardingValue shardingValue) {
        System.out.println("MyDBComplexKeysShardingAlgorithm：" + shardingValue.getColumnNameAndShardingValuesMap());
        System.out.println("MyDBComplexKeysShardingAlgorithm：" + StringUtils.collectionToDelimitedString(availableTargetNames,";"));
        return availableTargetNames;
    }
}
