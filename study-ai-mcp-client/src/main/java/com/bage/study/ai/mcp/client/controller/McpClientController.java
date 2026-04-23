package com.bage.study.ai.mcp.client.controller;

import com.bage.study.ai.mcp.client.service.McpClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mcp-client")
public class McpClientController {

    private final McpClientService mcpClientService;

    public McpClientController(McpClientService mcpClientService) {
        this.mcpClientService = mcpClientService;
    }

    @GetMapping("/hello")
    public String callHelloTool(@RequestParam(defaultValue = "World") String name) {
        return mcpClientService.callSayHelloTool(name);
    }
}
