import { Board, Card, Column } from '../shared/types';

const generateId = () => Math.random().toString(36).substring(2, 15);

class KanbanStore {
  private board: Board = {
    id: 'board-1',
    title: '项目看板',
    columns: [
      { id: 'col-backlog', title: '待办', status: 'backlog', color: '#9ca3af', cardIds: [] },
      { id: 'col-progress', title: '进行中', status: 'progress', color: '#3b82f6', cardIds: [] },
      { id: 'col-review', title: '审核中', status: 'review', color: '#f59e0b', cardIds: [] },
      { id: 'col-done', title: '已完成', status: 'done', color: '#10b981', cardIds: [] },
    ],
    cards: [],
  };

  getBoard(): Board {
    return this.board;
  }

  getCardsByStatus(status: string): Card[] {
    const column = this.board.columns.find(col => col.status === status);
    if (!column) return [];
    return column.cardIds.map(cardId => 
      this.board.cards.find(card => card.id === cardId)
    ).filter((card): card is Card => card !== undefined);
  }

  createCard(cardData: {
    title: string;
    description?: string;
    status?: string;
    priority?: 'low' | 'medium' | 'high' | 'urgent';
    assignee?: string;
    tags?: string[];
    dueDate?: string;
  }): Card {
    const now = new Date();
    const card: Card = {
      id: generateId(),
      title: cardData.title,
      description: cardData.description || '',
      status: cardData.status || 'backlog',
      priority: cardData.priority || 'medium',
      assignee: cardData.assignee,
      tags: cardData.tags || [],
      createdAt: now,
      updatedAt: now,
      dueDate: cardData.dueDate ? new Date(cardData.dueDate) : undefined,
    };

    this.board.cards.push(card);
    
    const column = this.board.columns.find(col => col.status === card.status);
    if (column) {
      column.cardIds.push(card.id);
    }

    return card;
  }

  moveCard(cardId: string, newStatus: string): Card | null {
    const card = this.board.cards.find(c => c.id === cardId);
    if (!card) return null;

    const oldColumn = this.board.columns.find(col => col.status === card.status);
    if (oldColumn) {
      oldColumn.cardIds = oldColumn.cardIds.filter(id => id !== cardId);
    }

    const newColumn = this.board.columns.find(col => col.status === newStatus);
    if (!newColumn) return null;

    card.status = newStatus;
    card.updatedAt = new Date();
    newColumn.cardIds.push(cardId);

    return card;
  }

  updateCard(cardId: string, updates: Partial<Card>): Card | null {
    const card = this.board.cards.find(c => c.id === cardId);
    if (!card) return null;

    Object.assign(card, updates);
    card.updatedAt = new Date();

    return card;
  }

  deleteCard(cardId: string): Card | null {
    const cardIndex = this.board.cards.findIndex(c => c.id === cardId);
    if (cardIndex === -1) return null;

    const card = this.board.cards[cardIndex];
    this.board.cards.splice(cardIndex, 1);

    const column = this.board.columns.find(col => col.status === card.status);
    if (column) {
      column.cardIds = column.cardIds.filter(id => id !== cardId);
    }

    return card;
  }
}

export const kanbanStore = new KanbanStore();