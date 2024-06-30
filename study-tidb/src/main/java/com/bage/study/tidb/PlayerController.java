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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @RequestMapping("/insert")
    public Object insert() {
        List<PlayerBean> list = new ArrayList<>();
        Random random = new Random();
        long id = System.currentTimeMillis();
        System.out.println("id = " + id);
        list.add(new PlayerBean(id, random.nextInt(1000), random.nextInt(1000)));
        Integer insert = playerService.createPlayers(list);
        System.out.println("insert = " + insert);
        return insert;
    }

    @RequestMapping("/query")
    public Object query(@RequestParam("id") Long id) {
        PlayerBean player = playerService.getPlayerByID(id);
        System.out.println("query = " + player);
        return player;
    }

    @GetMapping("/total")
    public Object total() {
        Long total = playerService.countPlayers();
        System.out.println("total = " + total);
        return total;
    }

}