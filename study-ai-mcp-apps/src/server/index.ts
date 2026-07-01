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
import {
  getBoardTool,
  listCardsTool,
  createCardTool,
  moveCardTool,
  updateCardTool,
  deleteCardTool,
  getKanbanUITool,
} from './tools/kanbanTools';
import { todoListHtml } from './ui/todoListHtml';
import { kanbanBoardHtml } from './ui/kanbanBoardHtml';
import { kanbanStore } from './kanbanStore';

kanbanStore.createCard({
  title: '设计系统架构',
  description: '完成项目整体架构设计，包括前端、后端和数据库',
  status: 'progress',
  priority: 'high',
  assignee: '张三',
  tags: ['架构', '设计'],
  dueDate: '2024-01-20',
});

kanbanStore.createCard({
  title: '编写API文档',
  description: '使用OpenAPI规范编写完整的API文档',
  status: 'review',
  priority: 'medium',
  assignee: '李四',
  tags: ['文档'],
  dueDate: '2024-01-18',
});

kanbanStore.createCard({
  title: '搭建开发环境',
  description: '配置开发环境，安装必要的依赖和工具',
  status: 'done',
  priority: 'medium',
  assignee: '王五',
  tags: ['环境'],
});

kanbanStore.createCard({
  title: '实现用户认证',
  description: '实现用户登录、注册和权限管理功能',
  status: 'backlog',
  priority: 'urgent',
  assignee: '张三',
  tags: ['认证', '安全'],
  dueDate: '2024-01-25',
});

kanbanStore.createCard({
  title: '设计数据库表结构',
  description: '设计和优化数据库表结构',
  status: 'backlog',
  priority: 'high',
  tags: ['数据库'],
});

kanbanStore.createCard({
  title: '前端组件开发',
  description: '开发通用UI组件库',
  status: 'progress',
  priority: 'medium',
  assignee: '赵六',
  tags: ['前端'],
});

const app = express();
const port = 3000;

app.use(cors());
app.use(express.json());

const createServer = () => {
  const server = new McpServer({
    name: 'mcp-app-kanban',
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

  server.registerTool(
    getBoardTool.name,
    getBoardTool.options,
    getBoardTool.handler,
  );

  server.registerTool(
    listCardsTool.name,
    listCardsTool.options,
    listCardsTool.handler,
  );

  server.registerTool(
    createCardTool.name,
    createCardTool.options,
    createCardTool.handler,
  );

  server.registerTool(
    moveCardTool.name,
    moveCardTool.options,
    moveCardTool.handler,
  );

  server.registerTool(
    updateCardTool.name,
    updateCardTool.options,
    updateCardTool.handler,
  );

  server.registerTool(
    deleteCardTool.name,
    deleteCardTool.options,
    deleteCardTool.handler,
  );

  server.registerTool(
    getKanbanUITool.name,
    getKanbanUITool.options,
    async () => {
      const uiResource = await createUIResource({
        uri: 'ui://kanban/board',
        content: { type: 'rawHtml', htmlString: kanbanBoardHtml },
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