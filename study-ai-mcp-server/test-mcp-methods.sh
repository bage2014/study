#!/bin/bash

# Test MCP Server with different method names
echo "Testing MCP Server with different method names..."

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

# Test 1: tool.list
echo "\n4. Testing tool.list..."
curl -s -X POST "http://localhost:8080/mcp/message?sessionId=$SESSION_ID" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"tool.list","params":{}}'

# Wait a moment for response
sleep 1

# Test 2: tool.call
echo "\n5. Testing tool.call..."
curl -s -X POST "http://localhost:8080/mcp/message?sessionId=$SESSION_ID" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":2,"method":"tool.call","params":{"toolCall":{"name":"sayHello","arguments":{"name":"MCP Test"}}}}'

# Wait a moment for response
sleep 1

# Test 3: list_tools
echo "\n6. Testing list_tools..."
curl -s -X POST "http://localhost:8080/mcp/message?sessionId=$SESSION_ID" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":3,"method":"list_tools","params":{}}'

# Wait a moment for response
sleep 1

# Test 4: call_tool
echo "\n7. Testing call_tool..."
curl -s -X POST "http://localhost:8080/mcp/message?sessionId=$SESSION_ID" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":4,"method":"call_tool","params":{"toolCall":{"name":"sayHello","arguments":{"name":"MCP Test"}}}}'

# Wait a moment for response
sleep 1

# Read the SSE response
echo "\n8. Reading SSE responses..."
SSE_RESPONSE=$(tail -50 "$temp_file")
echo "SSE response:\n$SSE_RESPONSE"

# Clean up
kill $CURL_PID 2>/dev/null
rm "$temp_file"

echo "\n9. MCP Server test completed!"
