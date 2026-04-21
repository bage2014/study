package com.bage.study.ai.mcp.server.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldTool {

    @Tool(name = "sayHello", description = "Returns a hello world message with the provided name")
    public String sayHello(@ToolParam(description = "The name to greet") String name) {
        return "Hello , " + name + "!";
    }
}
