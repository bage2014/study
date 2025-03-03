///*
// * Copyright 2016-2018 shardingsphere.io.
// * <p>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// * </p>
// */
//
//package com.bage.study.shadow;
//
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
//
//import java.util.Collection;
//
//public class PreciseModuloShardingDatabaseAlgorithm implements PreciseShardingAlgorithm<Long> {
//
//    @Override
//    public String doSharding(final Collection<String> databaseNames, final PreciseShardingValue<Long> shardingValue) {
////        System.out.println("shardingValue:" + shardingValue);
//        for (String each : databaseNames) {
//            if (each.endsWith((shardingValue.getValue() % 2 + 1) + "")) {
//                return each;
//            }
//        }
//        throw new UnsupportedOperationException();
//    }
//}
