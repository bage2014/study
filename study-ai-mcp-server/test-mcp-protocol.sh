#!/bin/bash

# Test MCP Server with correct MCP protocol methods
echo "Testing MCP Server with correct MCP protocol methods..."

# Create a temporary file to store SSE output
temp_file=$(mktemp)

# Start curl to connect to SSE endpoint in background
echo "\n1. Connecting to SSE endpoint..."
curl -s -N "http://localhost:8080/sse" > "$temp_file" &
CURL_PID=$!

# Wait a moment for connection to establish
sleep 1

# Read the first few lines to get session ID and endpoint
echo "\n2. Reading SSE data..."
SSE_DATA=$(head -10 "$temp_file")
echo "SSE data:\n$SSE_DATA"

# Extract session ID
SESSION_ID=$(echo "$SSE_DATA" | grep -E 'data:' | head -1 | cut -d '=' -f 2 | cut -d '?' -f 2 | cut -d '&' -f 1 | cut -d '=' -f 2)
echo "\n3. Session ID: $SESSION_ID"

if [ -z "$SESSION_ID" ]; then
  echo "Error: Could not get session ID"
  kill $CURL_PID 2>/dev/null
  rm "$temp_file"
  exit 1
fi

# Test tool discovery with correct method name
echo "\n4. Sending tools.list request..."
curl -s -X POST "http://localhost:8080/mcp/message?sessionId=$SESSION_ID" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools.list","params":{}}'

# Wait a moment for response
sleep 1

# Read the SSE response
echo "\n5. Reading SSE response..."
SSE_RESPONSE=$(tail -20 "$temp_file")
echo "SSE response:\n$SSE_RESPONSE"

# Test tool execution with correct method name
echo "\n6. Sending tools.call request..."
curl -s -X POST "http://localhost:8080/mcp/message?sessionId=$SESSION_ID" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":2,"method":"tools.call","params":{"toolCall":{"name":"sayHello","arguments":{"name":"MCP Test"}}}}'

# Wait a moment for response
sleep 1

# Read the SSE response
echo "\n7. Reading tool execution response..."
SSE_TOOL_RESPONSE=$(tail -30 "$temp_file")
echo "Tool execution response:\n$SSE_TOOL_RESPONSE"

# Clean up
kill $CURL_PID 2>/dev/null
rm "$temp_file"

echo "\n8. MCP Server test completed!"
