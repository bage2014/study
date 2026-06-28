import { z } from 'zod';
import { todoStore } from '../todoStore';

export const listTodosTool = {
  name: 'listTodos',
  options: {
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
  handler: async () => {
    const todos = todoStore.getAll();
    return {
      content: [{ type: 'text' as const, text: JSON.stringify(todos) }],
    };
  },
};

export const addTodoTool = {
  name: 'addTodo',
  options: {
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
  handler: async (params: { text: string }) => {
    const text = params.text;
    todoStore.add(text);
    return {
      content: [{ type: 'text' as const, text: '已添加任务: ' + text }],
    };
  },
};

export const toggleTodoTool = {
  name: 'toggleTodo',
  options: {
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
  handler: async (params: { id: string }) => {
    const id = params.id;
    const todo = todoStore.toggle(id);
    return {
      content: [{ type: 'text' as const, text: '已切换任务状态: ' + todo.text }],
    };
  },
};

export const deleteTodoTool = {
  name: 'deleteTodo',
  options: {
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
  handler: async (params: { id: string }) => {
    const id = params.id;
    const text = todoStore.delete(id);
    return {
      content: [{ type: 'text' as const, text: '已删除任务: ' + text }],
    };
  },
};

export const getUITool = {
  name: 'getUI',
  options: {
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
};