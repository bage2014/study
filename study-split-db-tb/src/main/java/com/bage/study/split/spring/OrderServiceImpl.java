/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bage.study.split.spring;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class OrderServiceImpl implements ExampleService {
    
    @Resource
    private OrderRepository orderRepository;
    @Override
    public void initEnvironment() throws SQLException {
        // orderRepository.createTableIfNotExists();
    }


    @Override
    public void cleanEnvironment() throws SQLException {
        // orderRepository.dropTable();
    }
    
    @Override
    @Transactional
    public void processSuccess() throws SQLException {
        System.out.println("-------------- Process Success Begin ---------------");
        List<Long> orderIds = insertData();
        printData();
        // deleteData(orderIds);
        // printData();
        System.out.println("-------------- Process Success Finish --------------");
    }
    
    @Override
    @Transactional
    public void processFailure() throws SQLException {
        System.out.println("-------------- Process Failure Begin ---------------");
        insertData();
        System.out.println("-------------- Process Failure Finish --------------");
        throw new RuntimeException("Exception occur for transaction test.");
    }

    private List<Long> insertData() throws SQLException {
        System.out.println("---------------------------- Insert Data ----------------------------");
        List<Long> result = new ArrayList<>(10);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (int i = 11; i <= 20; i++) {
            Order order = new Order();
            order.setOrderId(i);
            order.setUserId(i);
            order.setStatus("INSERT_TEST");
            order.setChannel("android");
            LocalDateTime ldt = LocalDateTime.parse("20" + (i > 5 ? 20 : 19) + "-02-10 16:30", dtf);
            order.setCreateTime(ldt);
            order.setSupplier("kkday");
            orderRepository.insert(order);
        }
        return result;
    }

    private void deleteData(final List<Long> orderIds) throws SQLException {
        System.out.println("---------------------------- Delete Data ----------------------------");
        for (Long each : orderIds) {
            orderRepository.delete(each);
        }
    }
    
    @Override
    public void printData() throws SQLException {
        System.out.println("---------------------------- Print Order Data -----------------------");
        for (Object each : orderRepository.queryByPage(null,1,10)) {
            System.out.println(each);
        }
    }
}
