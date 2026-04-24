#!/bin/bash

# Test MCP Server with standard MCP protocol format
echo "Testing MCP Server with standard MCP protocol format..."

# Connect to SSE endpoint and get session ID
echo "\n1. Connecting to SSE endpoint..."
SSE_RESPONSE=$(curl -s -m 3 -N "http://localhost:8080/sse" | head -3)
echo "SSE response:\n$SSE_RESPONSE"

# Extract session ID
SESSION_ID=$(echo "$SSE_RESPONSE" | grep -E 'data:' | cut -d '=' -f 2 | cut -d '?' -f 2 | cut -d '&' -f 1 | cut -d '=' -f 2)
echo "\n2. Session ID: $SESSION_ID"

if [ -z "$SESSION_ID" ]; then
  echo "Error: Could not get session ID"
  exit 1
fi

# Test with standard MCP protocol format
echo "\n3. Testing MCP protocol..."

# Create a test payload
cat > test-mcp-request.json << EOF
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "listTools",
  "params": {}
}
EOF

# Send the request
echo "Sending listTools request..."
curl -v -X POST "http://localhost:8080/mcp/message?sessionId=$SESSION_ID" \
  -H "Content-Type: application/json" \
  -d @test-mcp-request.json

# Clean up
rm test-mcp-request.json

echo "\n4. MCP Server test completed!"
