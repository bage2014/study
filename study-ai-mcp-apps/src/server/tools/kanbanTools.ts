import { z } from 'zod';
import { kanbanStore } from '../kanbanStore';

export const getBoardTool = {
  name: 'getBoard',
  options: {
    title: 'Get Board',
    description: '获取项目看板信息',
    inputSchema: z.object({}),
    _meta: {
      ui: {
        resourceUri: 'ui://kanban/board',
        visibility: ['model', 'app'],
      },
    },
  },
  handler: async () => {
    const board = kanbanStore.getBoard();
    return {
      content: [{ type: 'text' as const, text: JSON.stringify(board) }],
    };
  },
};

export const listCardsTool = {
  name: 'listCards',
  options: {
    title: 'List Cards',
    description: '获取所有卡片列表',
    inputSchema: z.object({
      status: z.string().optional().describe('按状态筛选'),
    }),
    _meta: {
      ui: {
        resourceUri: 'ui://kanban/board',
        visibility: ['model', 'app'],
      },
    },
  },
  handler: async (params: { status?: string }) => {
    const board = kanbanStore.getBoard();
    let cards = board.cards;
    if (params.status) {
      cards = kanbanStore.getCardsByStatus(params.status);
    }
    return {
      content: [{ type: 'text' as const, text: JSON.stringify(cards) }],
    };
  },
};

export const createCardTool = {
  name: 'createCard',
  options: {
    title: 'Create Card',
    description: '创建新卡片',
    inputSchema: z.object({
      title: z.string().describe('卡片标题'),
      description: z.string().optional().describe('卡片描述'),
      status: z.string().optional().describe('状态 (backlog/progress/review/done)'),
      priority: z.enum(['low', 'medium', 'high', 'urgent']).optional().describe('优先级'),
      assignee: z.string().optional().describe('负责人'),
      tags: z.array(z.string()).optional().describe('标签'),
      dueDate: z.string().optional().describe('截止日期'),
    }),
    _meta: {
      ui: {
        resourceUri: 'ui://kanban/board',
        visibility: ['model', 'app'],
      },
    },
  },
  handler: async (params: {
    title: string;
    description?: string;
    status?: string;
    priority?: 'low' | 'medium' | 'high' | 'urgent';
    assignee?: string;
    tags?: string[];
    dueDate?: string;
  }) => {
    const card = kanbanStore.createCard(params);
    return {
      content: [{ type: 'text' as const, text: '已创建卡片: ' + card.title }],
    };
  },
};

export const moveCardTool = {
  name: 'moveCard',
  options: {
    title: 'Move Card',
    description: '移动卡片到指定状态',
    inputSchema: z.object({
      cardId: z.string().describe('卡片ID'),
      newStatus: z.string().describe('新状态'),
    }),
    _meta: {
      ui: {
        resourceUri: 'ui://kanban/board',
        visibility: ['model', 'app'],
      },
    },
  },
  handler: async (params: { cardId: string; newStatus: string }) => {
    const card = kanbanStore.moveCard(params.cardId, params.newStatus);
    if (!card) {
      return {
        content: [{ type: 'text' as const, text: '移动失败: 卡片或状态不存在' }],
      };
    }
    return {
      content: [{ type: 'text' as const, text: '已移动卡片: ' + card.title + ' -> ' + params.newStatus }],
    };
  },
};

export const updateCardTool = {
  name: 'updateCard',
  options: {
    title: 'Update Card',
    description: '更新卡片信息',
    inputSchema: z.object({
      cardId: z.string().describe('卡片ID'),
      title: z.string().optional().describe('卡片标题'),
      description: z.string().optional().describe('卡片描述'),
      priority: z.enum(['low', 'medium', 'high', 'urgent']).optional().describe('优先级'),
      assignee: z.string().optional().describe('负责人'),
      tags: z.array(z.string()).optional().describe('标签'),
      dueDate: z.string().optional().describe('截止日期'),
    }),
    _meta: {
      ui: {
        resourceUri: 'ui://kanban/board',
        visibility: ['model', 'app'],
      },
    },
  },
  handler: async (params: {
    cardId: string;
    title?: string;
    description?: string;
    priority?: 'low' | 'medium' | 'high' | 'urgent';
    assignee?: string;
    tags?: string[];
    dueDate?: string;
  }) => {
    const { cardId, ...updates } = params;
    const card = kanbanStore.updateCard(cardId, updates);
    if (!card) {
      return {
        content: [{ type: 'text' as const, text: '更新失败: 卡片不存在' }],
      };
    }
    return {
      content: [{ type: 'text' as const, text: '已更新卡片: ' + card.title }],
    };
  },
};

export const deleteCardTool = {
  name: 'deleteCard',
  options: {
    title: 'Delete Card',
    description: '删除卡片',
    inputSchema: z.object({
      cardId: z.string().describe('卡片ID'),
    }),
    _meta: {
      ui: {
        resourceUri: 'ui://kanban/board',
        visibility: ['model', 'app'],
      },
    },
  },
  handler: async (params: { cardId: string }) => {
    const card = kanbanStore.deleteCard(params.cardId);
    if (!card) {
      return {
        content: [{ type: 'text' as const, text: '删除失败: 卡片不存在' }],
      };
    }
    return {
      content: [{ type: 'text' as const, text: '已删除卡片: ' + card.title }],
    };
  },
};

export const getKanbanUITool = {
  name: 'getKanbanUI',
  options: {
    title: 'Get Kanban UI',
    description: '获取项目看板的UI界面',
    inputSchema: z.object({}),
    _meta: {
      ui: {
        resourceUri: 'ui://kanban/board',
        visibility: ['model', 'app'],
      },
    },
  },
};