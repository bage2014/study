// Copyright 2022 PingCAP, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.bage.study.tidb;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SpringJpaHibernateApplicationTests {

    @Autowired
    private PlayerService playerService;

    @Test
    void createPlayers() {
        List<PlayerBean> list = new ArrayList<>();
        list.add(new PlayerBean(System.currentTimeMillis(), 1, 1));
        Integer count = playerService.createPlayers(list);
        System.out.println("count = " + count);
    }

    @Test
    void countPlayers() {
        Long count = playerService.countPlayers();
        System.out.println("count = " + count);
    }

}