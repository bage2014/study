package com.bage.study.jmeter.mysql;

import com.bage.study.jmeter.Customer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/mysql")
public class MySQLController {

    Random random = new Random();
    @Resource
    private CustomerRepo repo;

    @RequestMapping("/query")
    public Object query(@RequestParam(required = false) String key) {
        if (Objects.isNull(key)) {
            key = String.valueOf(System.currentTimeMillis() - random.nextInt());
        }
        System.out.println("query：" + LocalDateTime.now() + "; " + key);
        return repo.query(key);
    }

    @RequestMapping("/insert")
    public Object insert(@RequestParam(value = "prefix",required = false) String prefix,
                         @RequestParam(value = "size",required = false) Integer size) {
        if (Objects.isNull(prefix)) {
            prefix = String.valueOf(System.currentTimeMillis());
            prefix = prefix.substring(prefix.length() - 10);
        }
        if (Objects.isNull(size)) {
            size = 1;
        }
        List<Customer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new Customer(i, prefix + "" + random.nextInt(100),prefix + "" + random.nextInt(100)));
        }
        int result =  repo.insert(list);
        System.out.println("insert：" + "; " + prefix + "; actual = " + result + ", expect = " + list.size());
        return result;
    }

    @RequestMapping("/init")
    public String init() {
        repo.init();
        System.out.println("init：" + LocalDateTime.now());
        return "pang";
    }

}
