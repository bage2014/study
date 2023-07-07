package com.bage.study.split.basic;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.springframework.util.StringUtils;

import java.util.Collection;

public class MyTBComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm {
    @Override
    public Collection<String> doSharding(Collection availableTargetNames, ComplexKeysShardingValue shardingValue) {
        System.out.println("MyTBComplexKeysShardingAlgorithm：" + shardingValue.getColumnNameAndShardingValuesMap());
        System.out.println("MyTBComplexKeysShardingAlgorithm：" + StringUtils.collectionToDelimitedString(availableTargetNames,";"));
        return availableTargetNames;
    }
}
