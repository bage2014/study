package com.bage;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class AvailabilityController {

    private boolean validate(String console) {
        return "haha".equalsIgnoreCase(console);
//        return StringUtils.hasText(console) &&
//               Arrey("ps5", "ps4", "switch", "xbox").contains(console);
    }

    @GetMapping("/availability/{console}")
    Map<String, Object> getAvailability(@PathVariable String console) {
        return new HashMap<String, Object>();
//        return Map.of("console", console,
//                "available", checkAvailability(console));
    }

//    private boolean checkAvailability(String console) {
//        Assert.state(validate(console), () -> "the console specified, " + console + ", is not valid.");
//        return switch (console) {
//            case "ps5" -> throw new RuntimeException("Service exception");
//            case "xbox" -> true;
//            default -> false;
//        };
//    }
}