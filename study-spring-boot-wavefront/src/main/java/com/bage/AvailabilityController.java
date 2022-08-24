package com.bage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AvailabilityController {

    @GetMapping("/availability/{console}")
    Map<String, Object> getAvailability(@PathVariable String console) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("console", console);
        map.put("available", "true");
        return map;
    }

}