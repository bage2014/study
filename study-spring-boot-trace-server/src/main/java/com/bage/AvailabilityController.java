package com.bage;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class AvailabilityController {

    private boolean validate(String console) {
        Set set = new HashSet();
        set.addAll(Arrays.asList("ps5", "ps4", "switch", "xbox"));
        return StringUtils.hasText(console) &&
                set.contains(console);
    }

    @GetMapping("/availability/{console}")
    Map<String, Object> getAvailability(@PathVariable String console) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("console", console);
        map.put("available", checkAvailability(console));
        return map;
    }

    private boolean checkAvailability(String console) {
        Assert.state(validate(console), () -> "the console specified, " + console + ", is not valid.");
        boolean result = false;
        switch (console) {
            case "ps5":
                throw new RuntimeException("Service exception");
            case "xbox":
                result = true;
                break;
            default:
                result = false;
                break;
        }
        return result;
    }
}