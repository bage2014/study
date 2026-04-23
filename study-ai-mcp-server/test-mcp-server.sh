#!/bin/bash

# Test MCP Server SSE endpoint and message endpoint
echo "Testing MCP Server..."

# Get session ID from SSE response (timeout after 2 seconds)
echo "\n1. Getting session ID from SSE endpoint..."
SESSION_DATA=$(curl -s -m 2 -N "http://localhost:8080/sse" | head -3)
SESSION_ID=$(echo "$SESSION_DATA" | grep -E 'data:' | cut -d '=' -f 2 | cut -d '?' -f 2 | cut -d '&' -f 1 | cut -d '=' -f 2)

echo "Session ID: $SESSION_ID"

if [ -z "$SESSION_ID" ]; then
  echo "Error: Could not get session ID"
  exit 1
fi

# Test tool discovery
echo "\n2. Testing tool discovery..."
tools_response=$(curl -s -X POST "http://localhost:8080/mcp/message?sessionId=$SESSION_ID" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"listTools","params":{}}')

echo "Tools response: $tools_response"

# Test tool execution
echo "\n3. Testing tool execution..."
tool_response=$(curl -s -X POST "http://localhost:8080/mcp/message?sessionId=$SESSION_ID" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":2,"method":"callTool","params":{"toolCall":{"name":"sayHello","arguments":{"name":"MCP Test"}}}}')

echo "Tool response: $tool_response"

echo "\n4. MCP Server test completed!"
