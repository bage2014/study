//package com.bage.study.shadow;
//
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
//
//import java.util.Collection;
//
//public class PreciseModuloShardingTableAlgorithm implements PreciseShardingAlgorithm<java.sql.Timestamp> {
//
//    @Override
//    public String doSharding(final Collection<String> tableNames, final PreciseShardingValue<java.sql.Timestamp> shardingValue) {
//
//        if(tableNames.isEmpty()){
//            throw new UnsupportedOperationException();
//        }
//        return tableNames.stream().findFirst().get();
//        //        for (String each : tableNames) {
////            if (each.endsWith((shardingValue.getValue().getYear() + 1900) + "")) {
////                return each;
////            }
////        }
////
////        throw new UnsupportedOperationException();
////        System.out.println("shardingValue:" + shardingValue);
////        String[] objects = tableNames.toArray(new String[2]);
////
////        if (shardingValue.getValue() % 4 < 2) {
////            return objects[0];
////        }
////        return objects[1];
////        throw new UnsupportedOperationException();
//    }
//}