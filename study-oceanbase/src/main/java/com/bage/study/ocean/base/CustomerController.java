package com.bage.study.ocean.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerController {
    @Autowired
    private CustomerRepository repository;
    @GetMapping("/{id}")
    public Customer getOne(@PathVariable int id) {
        Customer obj = repository.findById(id);
        if(obj != null){
            String result = "Customer.id is:" + obj.getId() + ", no is :" + obj.getFirstName();
            log.info(result);
        }
        return obj;
    }
}
