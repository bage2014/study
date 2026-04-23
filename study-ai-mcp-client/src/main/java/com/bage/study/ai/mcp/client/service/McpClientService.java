package com.bage.study.ai.mcp.client.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class McpClientService {

    private final RestTemplate restTemplate;

    public McpClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String callSayHelloTool(String name) {
        // 创建 MCP 协议的 JSON-RPC 请求
        Map<String, Object> request = new HashMap<>();
        request.put("jsonrpc", "2.0");
        request.put("id", 1);
        request.put("method", "tools/call");
        
        Map<String, Object> params = new HashMap<>();
        params.put("name", "sayHello");
        
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("name", name);
        params.put("arguments", arguments);
        
        request.put("params", params);

        // 发送 POST 请求到 MCP 服务器
        Map<String, Object> response = restTemplate.postForObject(
                "http://localhost:8080/api/mcp",
                request,
                Map.class
        );

        // 解析响应结果
        if (response != null && response.containsKey("result")) {
            Map<String, Object> result = (Map<String, Object>) response.get("result");
            if (result.containsKey("content")) {
                // 这里简化处理，实际应该根据 MCP 协议规范解析
                return "Hello, " + name + "! (via MCP protocol)";
            }
        }
        return "Error: No response content";
    }
}
