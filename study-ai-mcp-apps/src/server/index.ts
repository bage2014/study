import express from 'express';
import cors from 'cors';
import { McpServer } from '@modelcontextprotocol/sdk/server/mcp';
import { StreamableHTTPServerTransport } from '@modelcontextprotocol/sdk/server/streamableHttp';
import { createUIResource } from '@mcp-ui/server';
import {
  listTodosTool,
  addTodoTool,
  toggleTodoTool,
  deleteTodoTool,
  getUITool,
} from './tools/todoTools';
import { todoListHtml } from './ui/todoListHtml';

const app = express();
const port = 3000;

app.use(cors());
app.use(express.json());

const createServer = () => {
  const server = new McpServer({
    name: 'mcp-app-todo',
    version: '1.0.0',
  });

  server.registerTool(
    listTodosTool.name,
    listTodosTool.options,
    listTodosTool.handler,
  );

  server.registerTool(
    addTodoTool.name,
    addTodoTool.options,
    addTodoTool.handler,
  );

  server.registerTool(
    toggleTodoTool.name,
    toggleTodoTool.options,
    toggleTodoTool.handler,
  );

  server.registerTool(
    deleteTodoTool.name,
    deleteTodoTool.options,
    deleteTodoTool.handler,
  );

  server.registerTool(
    getUITool.name,
    getUITool.options,
    async () => {
      const uiResource = await createUIResource({
        uri: 'ui://todo/list',
        content: { type: 'rawHtml', htmlString: todoListHtml },
        encoding: 'text',
      });
      return {
        content: [uiResource],
      };
    },
  );

  return server;
};

app.post('/mcp', async (req, res) => {
  const server = createServer();
  const transport = new StreamableHTTPServerTransport({
    sessionIdGenerator: undefined,
  });
  try {
    await server.connect(transport);
    await transport.handleRequest(req, res, req.body);
  } catch (error) {
    console.error('Error handling MCP request:', error);
    if (!res.headersSent) {
      res.status(500).json({
        jsonrpc: '2.0',
        error: {
          code: -32603,
          message: 'Internal server error',
        },
        id: null,
      });
    }
  }
});

app.get('/mcp', async (req, res) => {
  const server = createServer();
  const transport = new StreamableHTTPServerTransport({
    sessionIdGenerator: undefined,
  });
  try {
    await server.connect(transport);
    await transport.handleRequest(req, res);
  } catch (error) {
    console.error('Error handling MCP GET request:', error);
    if (!res.headersSent) {
      res.status(500).json({
        jsonrpc: '2.0',
        error: {
          code: -32603,
          message: 'Internal server error',
        },
        id: null,
      });
    }
  }
});

app.listen(port, () => {
  console.log('MCP Server listening at http://localhost:' + port + '/mcp');
});