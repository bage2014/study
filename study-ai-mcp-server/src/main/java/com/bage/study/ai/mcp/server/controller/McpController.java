package com.bage.study.ai.mcp.server.controller;

import com.bage.study.ai.mcp.server.tool.HelloWorldTool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mcp")
public class McpController {

    private final HelloWorldTool helloWorldTool;

    public McpController(HelloWorldTool helloWorldTool) {
        this.helloWorldTool = helloWorldTool;
    }

    @GetMapping("/hello")
    public String sayHello(@RequestParam(defaultValue = "World") String name) {
        return helloWorldTool.sayHello(name);
    }

    @GetMapping("/tools")
    public String[] getAvailableTools() {
        return new String[]{"sayHello"};
    }
}
