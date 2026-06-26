import express from 'express';
import cors from 'cors';
import { McpServer } from '@modelcontextprotocol/sdk/server/mcp';
import { StreamableHTTPServerTransport } from '@modelcontextprotocol/sdk/server/streamableHttp';
import { createMcpExpressApp } from '@modelcontextprotocol/sdk/server/express';
import { createUIResource } from '@mcp-ui/server';
import { z } from 'zod';
import { todoStore } from './todoStore.js';

const app = createMcpExpressApp({ host: '0.0.0.0' });
const port = 3000;

app.use(cors());

const todoListHtml = `
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Todo List</title>
  <style>
    body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background: #f5f5f5; }
    .todo-container { background: white; border-radius: 12px; padding: 24px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
    h1 { color: #1a1a1a; margin: 0 0 20px 0; font-size: 24px; }
    .input-row { display: flex; gap: 10px; margin-bottom: 20px; }
    input { flex: 1; padding: 12px; border: 1px solid #e0e0e0; border-radius: 8px; font-size: 16px; outline: none; transition: border-color 0.2s; }
    input:focus { border-color: #6366f1; }
    button { padding: 12px 20px; background: #6366f1; color: white; border: none; border-radius: 8px; cursor: pointer; font-size: 16px; transition: background 0.2s; }
    button:hover { background: #4f46e5; }
    button.delete { background: #ef4444; }
    button.delete:hover { background: #dc2626; }
    .todo-list { list-style: none; padding: 0; margin: 0; }
    .todo-item { display: flex; align-items: center; padding: 12px; border-bottom: 1px solid #f0f0f0; transition: background 0.2s; }
    .todo-item:last-child { border-bottom: none; }
    .todo-item:hover { background: #fafafa; }
    .todo-item.completed .text { text-decoration: line-through; color: #9ca3af; }
    input[type="checkbox"] { margin-right: 12px; width: 18px; height: 18px; cursor: pointer; }
    .text { flex: 1; font-size: 16px; color: #374151; }
    .empty { text-align: center; color: #9ca3af; padding: 40px 20px; }
  </style>
</head>
<body>
  <div class="todo-container">
    <h1>Todo List</h1>
    <div class="input-row">
      <input type="text" id="newTodo" placeholder="Add a new todo..." />
      <button onclick="addTodo()">Add</button>
    </div>
    <ul class="todo-list" id="todoList">
    </ul>
  </div>

  <script>
    function renderTodos(todos) {
      const list = document.getElementById('todoList');
      if (todos.length === 0) {
        list.innerHTML = '<li class="empty">No todos yet. Add one above!</li>';
        return;
      }
      list.innerHTML = todos.map(function(todo) {
        return '<li class="todo-item ' + (todo.completed ? 'completed' : '') + '">' +
          '<input type="checkbox" ' + (todo.completed ? 'checked' : '') + ' onchange="toggleTodo(\'' + todo.id + '\')" />' +
          '<span class="text">' + todo.text + '</span>' +
          '<button class="delete" onclick="deleteTodo(\'' + todo.id + '\')">Delete</button>' +
          '</li>';
      }).join('');
    }

    async function addTodo() {
      const input = document.getElementById('newTodo');
      const text = input.value.trim();
      if (!text) return;
      
      try {
        await window.mcp.callTool('addTodo', { text });
        input.value = '';
        await loadTodos();
      } catch (error) {
        console.error('Failed to add todo:', error);
      }
    }

    async function toggleTodo(id) {
      try {
        await window.mcp.callTool('toggleTodo', { id });
        await loadTodos();
      } catch (error) {
        console.error('Failed to toggle todo:', error);
      }
    }

    async function deleteTodo(id) {
      try {
        await window.mcp.callTool('deleteTodo', { id });
        await loadTodos();
      } catch (error) {
        console.error('Failed to delete todo:', error);
      }
    }

    async function loadTodos() {
      try {
        const result = await window.mcp.callTool('listTodos', {});
        const todos = JSON.parse(result.content[0].text);
        renderTodos(todos);
      } catch (error) {
        console.error('Failed to load todos:', error);
      }
    }

    document.getElementById('newTodo').addEventListener('keypress', function(e) {
      if (e.key === 'Enter') addTodo();
    });

    loadTodos();
  </script>
</body>
</html>
`;

const createServer = () => {
  const server = new McpServer({
    name: 'mcp-app-todo',
    version: '1.0.0',
  });

  server.registerTool(
    'listTodos',
    {
      title: 'List Todos',
      description: '获取所有待办事项列表',
      inputSchema: z.object({}),
      _meta: {
        ui: {
          resourceUri: 'ui://todo/list',
          visibility: ['model', 'app'],
        },
      },
    },
    async () => {
      const todos = todoStore.getAll();
      return {
        content: [{ type: 'text', text: JSON.stringify(todos) }],
      };
    },
  );

  server.registerTool(
    'addTodo',
    {
      title: 'Add Todo',
      description: '添加新的待办事项',
      inputSchema: z.object({
        text: z.string().describe('任务内容'),
      }),
      _meta: {
        ui: {
          resourceUri: 'ui://todo/list',
          visibility: ['model', 'app'],
        },
      },
    },
    async (params) => {
      const text = params.text as string;
      todoStore.add(text);
      return {
        content: [{ type: 'text', text: '已添加任务: ' + text }],
      };
    },
  );

  server.registerTool(
    'toggleTodo',
    {
      title: 'Toggle Todo',
      description: '切换待办事项的完成状态',
      inputSchema: z.object({
        id: z.string().describe('任务ID'),
      }),
      _meta: {
        ui: {
          resourceUri: 'ui://todo/list',
          visibility: ['model', 'app'],
        },
      },
    },
    async (params) => {
      const id = params.id as string;
      const todo = todoStore.toggle(id);
      return {
        content: [{ type: 'text', text: '已切换任务状态: ' + todo.text }],
      };
    },
  );

  server.registerTool(
    'deleteTodo',
    {
      title: 'Delete Todo',
      description: '删除待办事项',
      inputSchema: z.object({
        id: z.string().describe('任务ID'),
      }),
      _meta: {
        ui: {
          resourceUri: 'ui://todo/list',
          visibility: ['model', 'app'],
        },
      },
    },
    async (params) => {
      const id = params.id as string;
      const text = todoStore.delete(id);
      return {
        content: [{ type: 'text', text: '已删除任务: ' + text }],
      };
    },
  );

  server.registerTool(
    'getUI',
    {
      title: 'Get UI',
      description: '获取待办事项的UI界面',
      inputSchema: z.object({}),
      _meta: {
        ui: {
          resourceUri: 'ui://todo/list',
          visibility: ['model', 'app'],
        },
      },
    },
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
  try {
    const transport = new StreamableHTTPServerTransport({
      sessionIdGenerator: undefined,
    });
    await server.connect(transport);
    await transport.handleRequest(req, res, req.body);
    res.on('close', () => {
      transport.close();
      server.close();
    });
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
  try {
    const transport = new StreamableHTTPServerTransport({
      sessionIdGenerator: undefined,
    });
    await server.connect(transport);
    await transport.handleRequest(req, res);
    res.on('close', () => {
      transport.close();
      server.close();
    });
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